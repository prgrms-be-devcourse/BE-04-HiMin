package com.prgrms.himin.member.dto.request;

import static com.prgrms.himin.member.domain.Member.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record MemberLoginRequest(
	@Size(max = ID_MAX_LENGTH, message = "loginId는 최대 {max}글자입니다.")
	@NotBlank(message = "loginId가 비어있으면 안됩니다.")
	String loginId,

	@Size(max = PASSWORD_MAX_LENGTH, message = "loginId는 최대 {max}글자입니다.")
	@NotBlank(message = "비밀번호가 비어있으면 안됩니다.")
	String password
) {
}
