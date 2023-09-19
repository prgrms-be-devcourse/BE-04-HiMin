package com.prgrms.himin.delivery.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.prgrms.himin.delivery.domain.Rider;
import com.prgrms.himin.global.util.PhonePolicy;

public record RiderCreateRequest(
	@Size(max = 10, message = "이름은 최대 10글자 입니다.")
	@NotBlank(message = "이름이 비어있으면 안됩니다.")
	String name,

	@Size(max = 15, message = "핸드폰번호는 최대 15글자 입니다.")
	@NotBlank(message = "핸드폰번호가 비어있으면 안됩니다.")
	@Pattern(regexp = PhonePolicy.PHONE_PATTERN, message = "전화번호 형식이어야 합니다.")
	String phone
) {

	public Rider toEntity() {
		return new Rider(name, phone);
	}
}
