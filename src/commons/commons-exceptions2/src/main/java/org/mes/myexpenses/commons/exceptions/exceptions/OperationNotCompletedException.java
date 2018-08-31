package org.mes.myexpenses.commons.exceptions.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.EXPECTATION_FAILED)
public class OperationNotCompletedException extends GenericException {
	public OperationNotCompletedException() {
		super();
	}

	public OperationNotCompletedException(String detailed) {
		super(HttpStatus.EXPECTATION_FAILED.value(), "Operation not completed", detailed);
	}

	public OperationNotCompletedException(String message, String detailed) {
		super(HttpStatus.EXPECTATION_FAILED.value(), message, detailed);
	}

	public OperationNotCompletedException(Integer code, String message) {
		super(code, message, "");
	}

	public OperationNotCompletedException(Integer code, String message, String detailed) {
		super(code, message, detailed);
	}

	public OperationNotCompletedException(Integer code, String description, Throwable throwable) {
		super(code, description, throwable.getMessage());
	}

	public OperationNotCompletedException(String description, Throwable throwable) {
		super(0, description, throwable.getMessage());
	}
}