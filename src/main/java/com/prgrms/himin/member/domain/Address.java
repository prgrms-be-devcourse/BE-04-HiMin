package com.prgrms.himin.member.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.prgrms.himin.global.error.exception.ErrorCode;
import com.prgrms.himin.global.error.exception.InvalidValueException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "addresses")
public class Address {

	public static final int MAX_ADDRESS_LENGTH = 50;

	public static final int MAX_ADDRESS_ALIAS_LENGTH = 10;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long addressId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Column(name = "address_alias", nullable = false, length = MAX_ADDRESS_ALIAS_LENGTH)
	private String addressAlias;

	@Column(name = "address", nullable = false, length = MAX_ADDRESS_LENGTH)
	private String address;

	public Address(
		String addressAlias,
		String address
	) {
		validateAddressAlias(addressAlias);
		validateAddress(address);
		this.addressAlias = addressAlias;
		this.address = address;
	}

	public void attachTo(Member member) {
		if (this.member != null) {
			this.member.removeAddress(this);
		}
		this.member = member;
		member.addAddress(this);
	}

	public void updateAddress(
		String addressAlias,
		String address
	) {
		validateAddressAlias(addressAlias);
		validateAddress(address);
		this.addressAlias = addressAlias;
		this.address = address;
	}

	private void validateAddress(String address) {
		if (address == null || address.length() > MAX_ADDRESS_LENGTH) {
			throw new InvalidValueException(ErrorCode.MEMBER_ADDRESS_BAD_REQUEST);
		}
	}

	private void validateAddressAlias(String addressAlias) {
		if (addressAlias == null || addressAlias.length() > MAX_ADDRESS_ALIAS_LENGTH) {
			throw new InvalidValueException(ErrorCode.MEMBER_ADDRESS_ALIAS_BAD_REQUEST);
		}
	}
}
