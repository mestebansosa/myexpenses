package org.mes.myexpenses.commons.rest.config;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "server.servlet.session.cookie")
@ConditionalOnProperty(prefix = "server.servlet.session.cookie", name = "enabled", havingValue = "true")
public class CustomCookieManager {
	// properties (with default values)
	private String name = "myexpenses";
	private String comment = "myexpenses cookie";
	private String domain = "localhost";
	private Boolean httpOnly = true;
	private String maxAge = "30m";

	@PostConstruct
	public void init() {
		log.info(
				"CORS Configuration [allowedMethods={}, allowedHeaders={}, maxAge={}]",
				maxAge );
	}

    public Cookie createSessionCookie(final HttpServletResponse response, String sessionId) {
    	Cookie cookie  = new Cookie(name, sessionId);
    	cookie.setHttpOnly(httpOnly);
    	cookie.setMaxAge(30);
        return cookie;
    }

    // helper function to get session cookie
    public Cookie getSessionCookieActual(final HttpServletRequest request) {
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("session")) {
                return cookie;
            }
        }
        return null;
    }

}
