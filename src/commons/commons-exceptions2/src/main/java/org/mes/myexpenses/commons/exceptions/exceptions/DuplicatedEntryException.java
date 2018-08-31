package org.mes.myexpenses.commons.exceptions.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.CONFLICT)
public class DuplicatedEntryException extends GenericException {
	public DuplicatedEntryException() {
		super();
	}

	public DuplicatedEntryException(String detailed) {
		super(HttpStatus.CONFLICT.value(), "Already exists", detailed);
	}

	public DuplicatedEntryException(String message, String detailed) {
		super(HttpStatus.CONFLICT.value(), message, detailed);
	}

	public DuplicatedEntryException(Integer code, String message, String detailed) {
		super(code, message, detailed);
	}

	public DuplicatedEntryException(Integer code, String message) {
		super(code, message, "");
	}

	public DuplicatedEntryException(Integer code, String description, Throwable throwable) {
		super(code, description, throwable.getMessage());
	}

	public DuplicatedEntryException(String description, Throwable throwable) {
		super(0, description, throwable.getMessage());
	}
}