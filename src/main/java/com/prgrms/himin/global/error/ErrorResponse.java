package com.prgrms.himin.global.error;

import com.prgrms.himin.global.error.exception.ErrorCode;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {

	private final String error;
	private final String code;
	private final String message;

	public static ErrorResponse from(ErrorCode errorCode) {
		return new ErrorResponse(
			errorCode.name(),
			errorCode.getCode(),
			errorCode.getMessage()
		);
	}
}
