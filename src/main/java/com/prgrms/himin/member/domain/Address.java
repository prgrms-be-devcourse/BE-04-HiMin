package com.prgrms.himin.member.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "addresses")
public class Address {

	private static final int MAX_ADDRESS_LENGTH = 50;

	private static final int MAX_ADDRESS_ALIAS_LENGTH = 10;

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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Address address1 = (Address)o;

		return Objects.equals(addressId, address1.addressId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(addressId, member, addressAlias, address);
	}

	private void validateAddress(String address) {
		if (address == null || address.length() > MAX_ADDRESS_LENGTH) {
			throw new RuntimeException("잘못된 주소입니다.");
		}
	}

	private void validateAddressAlias(String addressAlias) {
		if (addressAlias == null || addressAlias.length() > MAX_ADDRESS_ALIAS_LENGTH) {
			throw new RuntimeException("잘못된 주소 가명입니다.");
		}
	}
}
