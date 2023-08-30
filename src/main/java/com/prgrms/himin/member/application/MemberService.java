package com.prgrms.himin.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.himin.member.domain.Address;
import com.prgrms.himin.member.domain.AddressRepository;
import com.prgrms.himin.member.domain.Member;
import com.prgrms.himin.member.domain.MemberRepository;
import com.prgrms.himin.member.dto.request.MemberCreateRequest;
import com.prgrms.himin.member.dto.response.MemberCreateResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

	private final MemberRepository memberRepository;

	private final AddressRepository addressRepository;

	@Transactional
	public MemberCreateResponse createMember(MemberCreateRequest request) {
		Member member = request.toEntity();
		Address address = new Address(
			member,
			request.getAddressAlias(),
			request.getAddress()
		);
		Member savedMember = memberRepository.save(member);
		Address savedAddress = addressRepository.save(address);
		return MemberCreateResponse.of(savedMember, savedAddress);
	}
}
