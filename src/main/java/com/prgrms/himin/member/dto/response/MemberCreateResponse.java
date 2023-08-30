package com.prgrms.himin.member.dto.response;

import java.time.LocalDate;

import com.prgrms.himin.member.domain.Address;
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

	private final Address address;

	@Builder
	public MemberCreateResponse(
		Long id,
		String loginId,
		String name,
		String phone,
		LocalDate birthday,
		Grade grade,
		Address address
	) {
		this.id = id;
		this.loginId = loginId;
		this.name = name;
		this.phone = phone;
		this.birthday = birthday;
		this.grade = grade;
		this.address = address;
	}

	public static MemberCreateResponse of(Member member, Address address) {
		return MemberCreateResponse.builder()
			.id(member.getId())
			.loginId(member.getLoginId())
			.name(member.getName())
			.phone(member.getPhone())
			.birthday(member.getBirthday())
			.grade(member.getGrade())
			.address(address)
			.build();
	}
}
