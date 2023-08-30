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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "addresses")
public class Address {

	private static final int MAX_ADDRESS_LENGTH = 50;

	private static final int MAX_ALIAS_ADDRESS_LENGTH = 10;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long addressId;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Column(name = "address_alias", nullable = false, length = MAX_ALIAS_ADDRESS_LENGTH)
	private String addressAlias;

	@Column(name = "address", nullable = false, length = MAX_ADDRESS_LENGTH)
	private String address;

	public Address(
		String addressAlias,
		String address
	) {
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
}
