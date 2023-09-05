package com.prgrms.himin.member.dto.request;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

public record MemberUpdateRequest() {

	public record Info(

		@Size(max = 20, message = "loginId은 최대 20글자 입니다.")
		@NotBlank(message = "loginId가 비어있으면 안됩니다.")
		String loginId,

		@Size(max = 20, message = "비밀번호는 최대 20글자 입니다.")
		@NotBlank(message = "비밀번호가 비어있으면 안됩니다.")
		String password,

		@Size(max = 10, message = "이름은 최대 10글자 입니다.")
		@NotBlank(message = "이름이 비어있으면 안됩니다.")
		String name,

		@Size(max = 15, message = "핸드폰번호는 최대 15자리 입니다.")
		@NotBlank(message = "핸드폰번호가 비어있으면 안됩니다.")
		String phone,

		@NotNull(message = "생일값이 null이면 안됩니다.")
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		LocalDate birthday
	) {
	}
}
