package com.prgrms.himin.member.dto.request;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.prgrms.himin.member.domain.Member;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public final class MemberCreateRequest {

	@Size(max = 20, message = "loginId은 최대 20글자 입니다.")
	@NotBlank(message = "loginId가 비어있으면 안됩니다.")
	private final String loginId;

	@Size(max = 20, message = "비밀번호는 최대 20글자 입니다.")
	@NotBlank(message = "비밀번호가 비어있으면 안됩니다.")
	private final String password;

	@Size(max = 10, message = "이름은 최대 20글자 입니다.")
	@NotBlank(message = "이름이 비어있으면 안됩니다.")
	private final String name;

	@Size(max = 15, message = "핸드폰번호는 최대 20글자 입니다.")
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
