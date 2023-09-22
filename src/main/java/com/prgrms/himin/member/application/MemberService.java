package com.prgrms.himin.member.application;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.himin.global.config.security.jwt.JwtAuthentication;
import com.prgrms.himin.global.config.security.jwt.JwtAuthenticationToken;
import com.prgrms.himin.global.error.exception.BusinessException;
import com.prgrms.himin.global.error.exception.EntityNotFoundException;
import com.prgrms.himin.global.error.exception.ErrorCode;
import com.prgrms.himin.global.error.exception.InvalidValueException;
import com.prgrms.himin.member.domain.Address;
import com.prgrms.himin.member.domain.AddressRepository;
import com.prgrms.himin.member.domain.Member;
import com.prgrms.himin.member.domain.MemberRepository;
import com.prgrms.himin.member.dto.request.AddressCreateRequest;
import com.prgrms.himin.member.dto.request.AddressUpdateRequest;
import com.prgrms.himin.member.dto.request.MemberCreateRequest;
import com.prgrms.himin.member.dto.request.MemberLoginRequest;
import com.prgrms.himin.member.dto.request.MemberUpdateRequest;
import com.prgrms.himin.member.dto.response.AddressResponse;
import com.prgrms.himin.member.dto.response.MemberCreateResponse;
import com.prgrms.himin.member.dto.response.MemberLoginResponse;
import com.prgrms.himin.member.dto.response.MemberResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

	private final MemberRepository memberRepository;

	private final AddressRepository addressRepository;

	private final PasswordEncoder passwordEncoder;

	private final AuthenticationManager authenticationManager;

	@Transactional
	public MemberCreateResponse createMember(MemberCreateRequest request) {
		String encodedPassword = passwordEncoder.encode(request.password());
		Member member = request.toEntity(encodedPassword);
		Address address = new Address(request.addressAlias(), request.address());
		address.attachTo(member);
		Member savedMember = memberRepository.save(member);

		return MemberCreateResponse.from(savedMember);
	}

	public MemberLoginResponse login(MemberLoginRequest request) {
		JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(request.loginId(), request.password());
		Authentication authenticated = authenticationManager.authenticate(authenticationToken);
		JwtAuthentication authentication = (JwtAuthentication)authenticated.getPrincipal();
		Member member = (Member)authenticated.getDetails();
		return new MemberLoginResponse(authentication.getToken(), member.getId(), member.getRoles());
	}

	public MemberResponse getMember(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(
				() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND)
			);
		List<AddressResponse> addresses = member.getAddresses()
			.stream()
			.map(AddressResponse::from)
			.toList();

		return MemberResponse.of(member, addresses);
	}

	@Transactional
	public void updateMember(
		Long memberId,
		MemberUpdateRequest.Info request
	) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(
				() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND)
			);

		String encodedPassword = passwordEncoder.encode(request.password());

		member.updateInfo(
			request.loginId(),
			Member.password(request.password(), encodedPassword),
			request.name(),
			request.phone(),
			request.birthday()
		);
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
		List<Address> addresses = addressRepository.findAddressesByMemberId(memberId);
		List<AddressResponse> addressResponses = addresses.stream()
			.map(AddressResponse::from)
			.toList();

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

	@Transactional
	public void updateAddress(
		Long memberId,
		Long addressId,
		AddressUpdateRequest request
	) {
		Address address = addressRepository.findById(addressId)
			.orElseThrow(
				() -> new InvalidValueException(ErrorCode.MEMBER_ADDRESS_BAD_REQUEST)
			);

		if (!address.getMember().getId().equals(memberId)) {
			throw new BusinessException(ErrorCode.MEMBER_ADDRESS_NOT_MATCH);
		}

		address.updateAddress(request.addressAlias(), request.address());
	}
}
