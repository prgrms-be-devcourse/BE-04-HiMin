package com.prgrms.himin.member.dto.response;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.prgrms.himin.member.domain.Grade;
import com.prgrms.himin.member.domain.Member;

import lombok.Builder;
import lombok.Getter;

@Getter
public final class MemberCreateResponse {

	private final Long id;

	private final String loginId;

	private final String name;

	private final String phone;

	private final LocalDate birthday;

	private final Grade grade;

	private final List<AddressResponse> addresses;

	@Builder
	private MemberCreateResponse(
		Long id,
		String loginId,
		String name,
		String phone,
		LocalDate birthday,
		Grade grade,
		List<AddressResponse> addresses
	) {
		this.id = id;
		this.loginId = loginId;
		this.name = name;
		this.phone = phone;
		this.birthday = birthday;
		this.grade = grade;
		this.addresses = addresses;
	}

	public static MemberCreateResponse from(Member member) {
		return MemberCreateResponse.builder()
			.id(member.getId())
			.loginId(member.getLoginId())
			.name(member.getName())
			.phone(member.getPhone())
			.birthday(member.getBirthday())
			.grade(member.getGrade())
			.addresses(member.getAddresses()
				.stream()
				.map(AddressResponse::from)
				.collect(Collectors.toList()))
			.build();
	}
}
