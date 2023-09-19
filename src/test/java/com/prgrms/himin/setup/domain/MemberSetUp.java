package com.prgrms.himin.setup.domain;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.prgrms.himin.member.domain.Address;
import com.prgrms.himin.member.domain.Member;
import com.prgrms.himin.member.domain.MemberRepository;
import com.prgrms.himin.member.dto.request.MemberCreateRequest;
import com.prgrms.himin.setup.request.MemberCreateRequestBuilder;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberSetUp {

	private final MemberRepository memberRepository;

	private final PasswordEncoder passwordEncoder;

	public Member saveOne() {
		MemberCreateRequest request = MemberCreateRequestBuilder.successBuild();
		String encodedPassword = passwordEncoder.encode(request.password());
		Member member = request.toEntity(encodedPassword);

		Address address = new Address(request.addressAlias(), request.address());
		address.attachTo(member);

		Member savedMember = memberRepository.save(member);

		return savedMember;
	}
}
