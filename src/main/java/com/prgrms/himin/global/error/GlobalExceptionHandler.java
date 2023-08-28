package com.prgrms.himin.global.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.prgrms.himin.global.error.exception.EntityNotFoundException;
import com.prgrms.himin.global.error.exception.ErrorCode;
import com.prgrms.himin.global.error.exception.InvalidValueException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponse> handleException(Exception e) {
		log.error("UnExpected Exception", e);
		ErrorResponse response = ErrorResponse.from(ErrorCode.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	protected ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
		log.error("EntityNotFoundException", e);
		ErrorResponse response = ErrorResponse.from(e.getErrorCode());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(InvalidValueException.class)
	protected ResponseEntity<ErrorResponse> handleInvalidValueException(InvalidValueException e) {
		log.error("InvalidValueException", e);
		ErrorResponse response = ErrorResponse.from(e.getErrorCode());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}
