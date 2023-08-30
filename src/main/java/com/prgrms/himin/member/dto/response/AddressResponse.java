package com.prgrms.himin.member.dto.response;

import com.prgrms.himin.member.domain.Address;

import lombok.Getter;

@Getter
public final class AddressResponse {

	private final Long addressId;

	private final String address;

	private final String addressAlias;

	private AddressResponse(
		Long addressId,
		String address,
		String addressAlias
	) {
		this.addressId = addressId;
		this.address = address;
		this.addressAlias = addressAlias;
	}

	public static AddressResponse from(Address address) {
		return new AddressResponse(
			address.getAddressId(),
			address.getAddress(),
			address.getAddressAlias()
		);
	}
}
