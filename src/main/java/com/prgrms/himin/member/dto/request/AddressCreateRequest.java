package com.prgrms.himin.member.dto.request;

import static com.prgrms.himin.member.domain.Address.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.prgrms.himin.member.domain.Address;

public record AddressCreateRequest(
	@Size(max = MAX_ADDRESS_ALIAS_LENGTH, message = "주소별칭은 최대 {max}글자 입니다.")
	@NotBlank(message = "주소별칭이 비어있으면 안됩니다.")
	String addressAlias,

	@Size(max = MAX_ADDRESS_LENGTH, message = "주소는 최대 {max}글자 입니다.")
	@NotBlank(message = "주소는 비어있으면 안됩니다.")
	String address
) {

	public Address toEntity() {
		return new Address(addressAlias, address);
	}
}
