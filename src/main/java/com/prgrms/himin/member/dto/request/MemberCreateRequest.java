package com.prgrms.himin.member.dto.request;

import static com.prgrms.himin.member.domain.Member.*;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.prgrms.himin.global.util.PhonePolicy;
import com.prgrms.himin.member.domain.Member;

public record MemberCreateRequest(
	@Size(max = ID_MAX_LENGTH, message = "loginId은 최대 {max}글자 입니다.")
	@NotBlank(message = "loginId가 비어있으면 안됩니다.")
	String loginId,

	@Size(max = PASSWORD_MAX_LENGTH, message = "비밀번호는 최대 {max}글자 입니다.")
	@NotBlank(message = "비밀번호가 비어있으면 안됩니다.")
	String password,

	@Size(max = NAME_MAX_LENGTH, message = "이름은 최대 {max}글자 입니다.")
	@NotBlank(message = "이름이 비어있으면 안됩니다.")
	String name,

	@Size(max = PHONE_MAX_LENGTH, message = "핸드폰번호는 최대 {max}글자 입니다.")
	@NotBlank(message = "핸드폰번호가 비어있으면 안됩니다.")
	@Pattern(regexp = PhonePolicy.PHONE_PATTERN, message = "전화번호 형식이어야 합니다.")
	String phone,

	@NotNull(message = "생일값이 null이면 안됩니다.")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	LocalDate birthday,

	@NotBlank(message = "주소 가명이 비어있으면 안됩니다.")
	String addressAlias,

	@NotBlank(message = "주소가 비어있으면 안됩니다.")
	String address
) {

	public Member toEntity(String encodedPassword) {
		return Member.builder()
			.loginId(loginId)
			.password(Member.password(password, encodedPassword))
			.name(name)
			.phone(phone)
			.birthday(birthday)
			.build();
	}
}
