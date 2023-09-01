package com.prgrms.himin.global.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	// Common
	INTERNAL_SERVER_ERROR("COMMON_001", "Internal Server Error"),
	INVALID_REQUEST("COMMON_002", "유효하지 않은 요청입니다."),

	// Member
	MEMBER_NOT_FOUND("MEMBER_001", "회원을 찾을 수 없습니다.");

	// Menu

	// Order

	// Shop

	private final String code;
	private final String message;
}
