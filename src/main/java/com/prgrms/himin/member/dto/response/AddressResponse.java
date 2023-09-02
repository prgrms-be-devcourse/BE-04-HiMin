package com.prgrms.himin.member.dto.response;

import com.prgrms.himin.member.domain.Address;

public record AddressResponse(
	Long addressId,
	String address,
	String addressAlias
) {

	public static AddressResponse from(Address address) {
		return new AddressResponse(
			address.getAddressId(),
			address.getAddress(),
			address.getAddressAlias()
		);
	}
}
