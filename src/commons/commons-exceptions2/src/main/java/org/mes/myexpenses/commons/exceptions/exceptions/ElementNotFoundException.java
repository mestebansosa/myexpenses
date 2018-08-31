package org.mes.myexpenses.commons.exceptions.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ElementNotFoundException extends GenericException {
	public ElementNotFoundException() {
		super();
	}

	public ElementNotFoundException(String detailed) {
		super(HttpStatus.NOT_FOUND.value(), "No value present", detailed);
	}

	public ElementNotFoundException(String message, String detailed) {
		super(HttpStatus.NOT_FOUND.value(), message, detailed);
	}

	public ElementNotFoundException(Integer code, String message) {
		super(code, message, "");
	}

	public ElementNotFoundException(Integer code, String message, String detailed) {
		super(code, message, detailed);
	}

	public ElementNotFoundException(Integer code, String description, Throwable throwable) {
		super(code, description, throwable.getMessage());
	}

	public ElementNotFoundException(String description, Throwable throwable) {
		super(0, description, throwable.getMessage());
	}
}