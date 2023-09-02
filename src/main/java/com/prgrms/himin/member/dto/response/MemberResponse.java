package com.prgrms.himin.member.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.prgrms.himin.member.domain.Grade;
import com.prgrms.himin.member.domain.Member;

import lombok.Builder;

@Builder
public record MemberResponse(
	Long id,
	String loginId,
	String name,
	String phone,
	LocalDate birthday,
	Grade grade,
	List<AddressResponse> addresses
) {

	public static MemberResponse of(Member member, List<AddressResponse> addresses) {
		return MemberResponse.builder()
			.id(member.getId())
			.loginId(member.getLoginId())
			.name(member.getName())
			.phone(member.getPhone())
			.birthday(member.getBirthday())
			.grade(member.getGrade())
			.addresses(addresses)
			.build();
	}
}
