package com.prgrms.himin.global.error;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.prgrms.himin.global.error.exception.ErrorCode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {

	private final String error;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private final List<FieldError> errors;

	private final String code;

	private final String message;

	public static ErrorResponse from(ErrorCode errorCode) {
		return new ErrorResponse(
			errorCode.name(),
			null,
			errorCode.getCode(),
			errorCode.getMessage()
		);
	}

	public static ErrorResponse of(ErrorCode errorCode, BindingResult bindingResult) {
		return new ErrorResponse(
			errorCode.name(),
			FieldError.from(bindingResult),
			errorCode.getCode(),
			errorCode.getMessage()
		);
	}

	@Getter
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	public static class FieldError {

		private final String field;
		private final String value;
		private final String reason;

		private static List<FieldError> from(BindingResult bindingResult) {
			List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
			return fieldErrors.stream()
				.map(
					error -> new FieldError(
						error.getField(),
						error.getRejectedValue() == null ? null : error.getRejectedValue().toString(),
						error.getDefaultMessage()
					))
				.collect(Collectors.toList());
		}
	}
}
