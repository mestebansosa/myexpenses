package org.mes.myexpenses.bs.administration.service;

import java.net.URI;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mes.myexpenses.bs.administration.domains.Login;
import org.mes.myexpenses.bs.administration.domains.Session;
import org.mes.myexpenses.bs.administration.domains.User;
import org.mes.myexpenses.commons.exceptions.exceptions.LoginException;
import org.mes.myexpenses.commons.exceptions.exceptions.OperationNotCompletedException;
import org.mes.myexpenses.commons.rest.config.RestTemplateClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "myexpenses.dataservices.administration")
@Service
@Data
public class AdministrationImpl implements Administration {
	// Mongo DB Properties
	private String host;
	private String usersPath;
	private String sessionsPath;
	private String mongoPath;
	private int port;

	private UriComponentsBuilder uriComponentsBuilderUsers;
	private UriComponentsBuilder uriComponentsBuilderSessions;
	private UriComponentsBuilder uriComponentsBuilderMongo;

	private final RestTemplateClient restTemplateClient;

	public AdministrationImpl(RestTemplateClient restTemplateClient) {
		this.restTemplateClient = restTemplateClient;
		
		}

	@PostConstruct
	public void init() {
		log.info("AdministrationImpl [host={}, port={}, path={}]", host, port, usersPath);
		UriComponentsBuilder uriCB = UriComponentsBuilder.newInstance().scheme("http").host(host).port(port);
		uriComponentsBuilderUsers = uriCB.cloneBuilder().path(usersPath);
		uriComponentsBuilderSessions = uriCB.cloneBuilder().path(sessionsPath);
		uriComponentsBuilderMongo = uriCB.cloneBuilder().path(mongoPath);
	}

	@Override
	public void login(Login login, String sessionId) throws OperationNotCompletedException {		
		validateUser(login);
		sessionChecking(login.getId(), sessionId);
	}

	@Override
	public void logout(Login login, String sessionId, HttpServletRequest request, HttpServletResponse response) throws OperationNotCompletedException {
		sessionCheckout(sessionId);
		deleteResponseCookie(sessionId, request, response);
	}

	@Override
	public void signup(User user, String sessionId) throws LoginException, OperationNotCompletedException {
		if (existsUser(user)) {
			throw new LoginException("Username already in use",
					user.getId() + " already in use, Please choose another");
		}

		User userResponse = saveUser(user);
		if (!user.compareSignup(userResponse)) {
			String detailed = String.format("%s %s not saved.", "signup", user.getId());
			log.error(detailed);
			throw new OperationNotCompletedException(detailed);
		}
		sessionChecking(user.getId(), sessionId);

	}

	/**
	 * The cookie is created automatically by server.servlet.session. 
	 * The cookie.sessionId will be used as id in the myexpenses.session collection.
	 */
	public void sessionChecking(String user, String sessionId) throws OperationNotCompletedException {
		if (existsSession(sessionId)) {
			log.info("Session: {} already exists", sessionId);
		}
		else {
			saveSession(user, sessionId);
		}
		
		updateCandidateSessions(user, sessionId);		
	}

	public void sessionCheckout(String sessionId) throws OperationNotCompletedException {
		Session session = getSession(sessionId);
		session.setEnd(new Date());
		saveSession(session);
	}
	
	private Boolean existsUser(User user) {
		UriComponentsBuilder uriCB = uriComponentsBuilderUsers.cloneBuilder();
		uriCB.path("/").path("exists");
		uriCB.path("/").path(user.getId());
		URI url = uriCB.build().toUri();

		log.info("existsUser: Checking {} if exists user with {}", url.toASCIIString(), user);
		return restTemplateClient.getForEntity(url, Boolean.class, "existsUser");
	}

	public User saveUser(User user) {
		UriComponentsBuilder uriCB = uriComponentsBuilderUsers.cloneBuilder();
		URI url = uriCB.build().toUri();

		log.info("saveUser: Creating user with {}, {}", url.toASCIIString(), user);
		return restTemplateClient.postForObject(url, User.class, user, "getUser");
	}

	public void validateUser(Login login) {
		UriComponentsBuilder uriCB = uriComponentsBuilderUsers.cloneBuilder();
		uriCB.path("/").path("validate");
		URI url = uriCB.build().toUri();

		log.info("validateUser: Validate {} user with name {}", url.toASCIIString(), login.getId());
		restTemplateClient.postForObject(url, Login.class, login, "validateUser");
	}

	private Boolean existsSession(String sessionId) {
		UriComponentsBuilder uriCB = uriComponentsBuilderSessions.cloneBuilder();
		uriCB.path("/").path("exists");
		uriCB.path("/").path(sessionId);
		URI url = uriCB.build().toUri();

		log.info("Checking if exists session {}", url.toASCIIString());
		return restTemplateClient.getForEntity(url, Boolean.class, "existsSession");
	}
	
	public Session getSession(String sessionId) {
		UriComponentsBuilder uriCB = uriComponentsBuilderSessions.cloneBuilder();
		uriCB.path("/").path(sessionId);
		URI url = uriCB.build().toUri();

		log.info("Getting sessionId {}", url.toASCIIString());
		return restTemplateClient.getForEntity(url, Session.class, "getSession");		
	}

	
	private void saveSession(String name, String sessionId) throws OperationNotCompletedException {
		UriComponentsBuilder uriCB = uriComponentsBuilderSessions.cloneBuilder();
		URI url = uriCB.build().toUri();

		Session session = new Session();
		session.setUser(name);
		session.setId(sessionId);
		session.setStart(new Date());
		log.info("Creating user session {}, user {}", url.toASCIIString(), name);
		Session sessionResponse = restTemplateClient.postForObject(url, Session.class, session, "signup");
		if (!session.getUser().contentEquals(sessionResponse.getUser())) {
			String detailed = String.format("%s %s %s not saved.", "saveSession", name, sessionId);
			log.error(detailed);
			throw new OperationNotCompletedException(detailed);
		}				
	}

	private void saveSession(Session session) throws OperationNotCompletedException {
		UriComponentsBuilder uriCB = uriComponentsBuilderSessions.cloneBuilder();
		URI url = uriCB.build().toUri();

		session.setEnd(new Date());
		log.info("Updating user session {}, user {}", url.toASCIIString(), session.getId());
		Session sessionResponse = restTemplateClient.postForObject(url, Session.class, session, "signup");
		if (!session.getUser().contentEquals(sessionResponse.getUser())) {
			String detailed = String.format("%s %s not saved.", "saveSession", session.getUser());
			log.error(detailed);
			throw new OperationNotCompletedException(detailed);
		}				
	}

	/**
	 * Update the endDate to all the sessions belonging to one user that are not equal to the current sesssionId, and have no endDate.
	 * @param name
	 * @param sessionId
	 * @throws Exception
	 */
	private void updateCandidateSessions(String name, String sessionId) {
		UriComponentsBuilder uriCB = uriComponentsBuilderMongo.cloneBuilder();
		uriCB.path("/").path("updateCandidateSessions");
		URI url = uriCB.build().toUri();

		Session session = new Session();
		session.setUser(name);
		session.setId(sessionId);
		log.info("updateCandidateSessions {}, user {}", url.toASCIIString(), name);
		restTemplateClient.put(url, Session.class, session, "signup");
	}

    // helper function to get session cookie as string
    private void deleteResponseCookie(final String sessionId, final HttpServletRequest request, final HttpServletResponse response) {
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(sessionId)) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
                break;
            }
        }
    }

}
