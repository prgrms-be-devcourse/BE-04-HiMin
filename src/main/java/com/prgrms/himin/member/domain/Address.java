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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "addresses")
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long addressId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member", nullable = false)
	private Member member;

	private String addressAlias;

	private String address;

	public Address(
		Member member,
		String addressAlias,
		String address
	) {
		this.member = member;
		this.addressAlias = addressAlias;
		this.address = address;
	}
}
