package org.mes.myexpenses.commons.exceptions.exceptions;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GenericExceptionHandler {

	/**
	 * MyExpenses exceptions
	 */
	@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
	@ExceptionHandler({ LoginException.class })
	@ResponseBody
	public GenericException handleLogin(LoginException e) {
		return e;
	}

	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	@ExceptionHandler({ ElementNotFoundException.class })
	public GenericException handleElementNotFound(ElementNotFoundException e) {
		return e;
	}

	@ResponseStatus(code = HttpStatus.CONFLICT)
	@ExceptionHandler({ DuplicatedEntryException.class })
	public GenericException handleDuplicatedEntry(DuplicatedEntryException e) {
		return e;
	}

	@ResponseStatus(code = HttpStatus.EXPECTATION_FAILED)
	@ExceptionHandler({ OperationNotCompletedException.class })
	public GenericException handleOperationNotCompleted(OperationNotCompletedException e) {
		return e;
	}

	/**
	 * Spring framework exception
	 */
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ HttpMessageNotReadableException.class })
	public GenericException handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
		return GenericException.builder().code(HttpStatus.BAD_REQUEST.value()).message("JSON parse error")
				.detailed(e.getMessage()).build();
	}
	
	@ResponseStatus(code = HttpStatus.PRECONDITION_FAILED)
	@ExceptionHandler({ MethodArgumentNotValidException.class })
	public GenericException handleMethodArgumentNotValid(Exception e) {
		log.error("{} {}", "Validation error: ", e.getMessage());
		return GenericException.builder().code(HttpStatus.PRECONDITION_FAILED.value()).message("Validation error").detailed(e.getMessage()).build();
	}

	/**
	 * MongoRepository exception
	 */
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	@ExceptionHandler({ NoSuchElementException.class })
	public GenericException handleNoSuchElement(NoSuchElementException e) {
		return GenericException.builder().code(HttpStatus.NOT_FOUND.value()).message(e.getMessage())
				.detailed(e.getLocalizedMessage()).build();
	}

	/**
	 * This is the default exception between services when calling by restTemplate.
	 * Exception thrown when an HTTP 4xx or 5xx is received.
	 */
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({ HttpClientErrorException.class, HttpServerErrorException.class })
	public GenericException handleHttpClientError(HttpStatusCodeException e) {
		return GenericException.builder().code(e.getStatusCode().value()).message(e.getMessage())
				.detailed(e.getResponseBodyAsString()).build();
	}	
	
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({ Exception.class, RuntimeException.class })
	public GenericException handleAllExceptions(Exception e) {
		log.error("{} {}", "Internal error: ", e.getMessage());
		e.printStackTrace();
		return GenericException.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message("Internal error").detailed(e.getMessage()).build();
	}
}
