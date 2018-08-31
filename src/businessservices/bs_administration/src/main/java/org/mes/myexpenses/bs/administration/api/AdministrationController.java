package org.mes.myexpenses.bs.administration.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.mes.myexpenses.bs.administration.constants.MyExpensesConstants;
import org.mes.myexpenses.bs.administration.domains.Login;
import org.mes.myexpenses.bs.administration.domains.User;
import org.mes.myexpenses.bs.administration.service.Administration;
import org.mes.myexpenses.bs.administration.service.AdministrationImpl;
import org.mes.myexpenses.commons.exceptions.exceptions.LoginException;
import org.mes.myexpenses.commons.exceptions.exceptions.OperationNotCompletedException;
import org.mes.myexpenses.commons.rest.metrics.ActuatorMeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class AdministrationController {

	private final Administration administration;
	private final ActuatorMeterRegistry actuatorMeterRegistry;

	public AdministrationController(AdministrationImpl administration, ActuatorMeterRegistry actuatorMeterRegistry) {
		this.administration = administration;
		this.actuatorMeterRegistry = actuatorMeterRegistry;
		actuatorMeterRegistry.createCounters(
				actuatorMeterRegistry.getMethodNames(this.getClass(), MyExpensesConstants.ENDPOINTS.COUNTER_ADMIN));
	}

	// cookie is created automatically by server.servlet.session. 
	// The sessionId will be used as id in the myexpenses.session collection.

	@PostMapping(value = MyExpensesConstants.ENDPOINTS.SIGNUP)
	@ResponseStatus(HttpStatus.OK)
	public void signup(@RequestBody @Valid User user, HttpServletRequest request, HttpServletResponse response) throws LoginException, OperationNotCompletedException {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_ADMIN_SIGNUP;
		log.info("{} {}", methodName, user);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		HttpSession httpSession = request.getSession();
		httpSession.setMaxInactiveInterval(60);
		log.info("signup: SessionId:{} Signup. User {}", request.getSession().getId(), user);
		administration.signup(user, httpSession.getId());
	}
	
	@PostMapping(value = MyExpensesConstants.ENDPOINTS.LOGIN)
	@ResponseStatus(HttpStatus.OK)
	public void login(@RequestBody @Valid Login login, HttpServletRequest request) throws OperationNotCompletedException {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_ADMIN_LOGIN;
		log.info("{} {}", methodName, login);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		HttpSession httpSession = request.getSession();
		httpSession.setMaxInactiveInterval(60);
		log.info("login: SessionId:{} Login. {}", request.getSession().getId(), login);
		administration.login(login, httpSession.getId());
	}

	@PostMapping(value = MyExpensesConstants.ENDPOINTS.LOGOUT)
	@ResponseStatus(HttpStatus.OK)
	public void logout(@RequestBody @Valid Login login, HttpServletRequest request, HttpServletResponse response) throws OperationNotCompletedException {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_ADMIN_LOGOUT;
		log.info("{} {}", methodName, login);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		HttpSession httpSession = request.getSession();
		httpSession.setMaxInactiveInterval(60);
		log.info("logout: SessionId:{} Login. {}", request.getSession().getId(), login);
		administration.logout(login, httpSession.getId(), request, response);
	}


}