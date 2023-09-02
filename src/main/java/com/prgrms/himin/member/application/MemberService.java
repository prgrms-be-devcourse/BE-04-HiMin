package com.prgrms.himin.member.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.himin.global.error.exception.BusinessException;
import com.prgrms.himin.global.error.exception.EntityNotFoundException;
import com.prgrms.himin.global.error.exception.ErrorCode;
import com.prgrms.himin.global.error.exception.InvalidValueException;
import com.prgrms.himin.member.domain.Address;
import com.prgrms.himin.member.domain.AddressRepository;
import com.prgrms.himin.member.domain.Member;
import com.prgrms.himin.member.domain.MemberRepository;
import com.prgrms.himin.member.dto.request.AddressCreateRequest;
import com.prgrms.himin.member.dto.request.MemberCreateRequest;
import com.prgrms.himin.member.dto.request.MemberLoginRequest;
import com.prgrms.himin.member.dto.response.AddressResponse;
import com.prgrms.himin.member.dto.response.MemberCreateResponse;
import com.prgrms.himin.member.dto.response.MemberResponse;

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
			request.addressAlias(),
			request.address()
		);
		address.attachTo(member);
		Member savedMember = memberRepository.save(member);

		return MemberCreateResponse.from(savedMember);
	}

	public void login(MemberLoginRequest request) {
		if (!memberRepository.existsMemberByLoginIdAndPassword(
			request.loginId(),
			request.password()
		)) {
			throw new BusinessException(ErrorCode.MEMBER_LOGIN_FAIL);
		}
	}

	public MemberResponse getMember(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(
				() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND)
			);
		List<AddressResponse> addresses = member.getAddresses()
			.stream()
			.map(AddressResponse::from)
			.collect(Collectors.toList());

		return MemberResponse.of(member, addresses);
	}

	@Transactional
	public void deleteMember(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(
				() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND)
			);

		memberRepository.delete(member);
	}

	@Transactional
	public AddressResponse createAddress(
		Long memberId,
		AddressCreateRequest request
	) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(
				() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND)
			);
		Address address = request.toEntity();
		address.attachTo(member);
		Address savedAddress = addressRepository.save(address);

		return AddressResponse.from(savedAddress);
	}

	public List<AddressResponse> getAllAddress(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(
				() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND)
			);
		List<Address> addresses = member.getAddresses();
		List<AddressResponse> addressResponses = addresses.stream()
			.map(AddressResponse::from)
			.collect(Collectors.toList());

		return addressResponses;
	}

	@Transactional
	public void deleteAddress(
		Long memberId,
		Long addressId
	) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(
				() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND)
			);
		Address address = addressRepository.findById(addressId)
			.orElseThrow(
				() -> new InvalidValueException(ErrorCode.MEMBER_ADDRESS_BAD_REQUEST)
			);

		if (!member.removeAddress(address)) {
			throw new BusinessException(ErrorCode.MEMBER_ADDRESS_NOT_MATCH);
		}
	}
}
