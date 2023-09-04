package com.prgrms.himin.setup.domain;

import org.springframework.stereotype.Component;

import com.prgrms.himin.member.domain.Member;
import com.prgrms.himin.member.domain.MemberRepository;
import com.prgrms.himin.member.dto.request.MemberCreateRequest;
import com.prgrms.himin.setup.request.MemberCreateRequestBuilder;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberSetUp {

	private final MemberRepository memberRepository;

	public Member saveOne() {
		MemberCreateRequest request = MemberCreateRequestBuilder.successBuild();
		Member savedMember = memberRepository.save(request.toEntity());

		return savedMember;
	}
}
