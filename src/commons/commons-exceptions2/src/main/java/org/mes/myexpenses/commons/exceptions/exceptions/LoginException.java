package org.mes.myexpenses.commons.exceptions.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class LoginException extends GenericException {
	public LoginException() {
		super();
	}

	public LoginException(String detailed) {
		super(HttpStatus.UNAUTHORIZED.value(), "Login not found", detailed);
	}

	public LoginException(String message, String detailed) {
		super(HttpStatus.UNAUTHORIZED.value(), message, detailed);
	}

	public LoginException(Integer code, String message) {
		super(code, message, "");
	}

	public LoginException(Integer code, String message, String detailed) {
		super(code, message, detailed);
	}

	public LoginException(Integer code, String description, Throwable throwable) {
		super(code, description, throwable.getMessage());
	}

	public LoginException(String description, Throwable throwable) {
		super(0, description, throwable.getMessage());
	}
}