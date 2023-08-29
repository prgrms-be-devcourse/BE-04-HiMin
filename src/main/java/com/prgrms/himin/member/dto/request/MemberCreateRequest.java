package com.prgrms.himin.member.dto.request;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.prgrms.himin.member.domain.Member;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public final class MemberCreateRequest {

	@NotBlank(message = "loginId가 비어있으면 안됩니다.")
	private final String loginId;

	@NotBlank(message = "비밀번호가 비어있으면 안됩니다.")
	private final String password;

	@NotBlank(message = "이름이 비어있으면 안됩니다.")
	private final String name;

	@NotBlank(message = "핸드폰번호가 비어있으면 안됩니다.")
	private final String phone;

	@NotNull(message = "생일값이 null이면 안됩니다.")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private final LocalDate birthday;

	@NotBlank(message = "주소 가명이 비어있으면 안됩니다.")
	private final String addressAlias;

	@NotBlank(message = "주소가 비어있으면 안됩니다.")
	private final String address;

	public Member toEntity() {
		return Member.builder()
			.loginId(loginId)
			.password(password)
			.name(name)
			.phone(phone)
			.birthday(birthday)
			.build();
	}
}
