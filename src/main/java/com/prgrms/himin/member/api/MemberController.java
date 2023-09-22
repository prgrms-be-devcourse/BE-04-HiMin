package com.prgrms.himin.member.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.himin.member.application.MemberService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/sign-up")
	public ResponseEntity<MemberCreateResponse> createMember(@Valid @RequestBody MemberCreateRequest request) {
		MemberCreateResponse response = memberService.createMember(request);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/sign-in")
	public ResponseEntity<MemberLoginResponse> login(@Valid @RequestBody MemberLoginRequest request) {
		MemberLoginResponse response = memberService.login(request);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/members/{memberId}")
	public ResponseEntity<MemberResponse> getMember(@PathVariable Long memberId) {
		MemberResponse response = memberService.getMember(memberId);

		return ResponseEntity.ok(response);
	}

	@PutMapping("/members/{memberId}")
	public ResponseEntity<Void> updateMember(
		@PathVariable Long memberId,
		@Valid @RequestBody MemberUpdateRequest.Info request
	) {
		memberService.updateMember(memberId, request);

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/withdrawal/{memberId}")
	public ResponseEntity<Void> deleteMember(@PathVariable Long memberId) {
		memberService.deleteMember(memberId);

		return ResponseEntity.ok().build();
	}

	@PostMapping("/members/{memberId}/addresses")
	public ResponseEntity<AddressResponse> createAddress(
		@PathVariable Long memberId,
		@Valid @RequestBody AddressCreateRequest request
	) {
		AddressResponse response = memberService.createAddress(memberId, request);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/members/{memberId}/addresses")
	public ResponseEntity<List<AddressResponse>> getAllAddress(@PathVariable Long memberId) {
		List<AddressResponse> response = memberService.getAllAddress(memberId);

		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/members/{memberId}/addresses/{addressId}")
	public ResponseEntity<Void> deleteAddress(
		@PathVariable Long memberId,
		@PathVariable Long addressId
	) {
		memberService.deleteAddress(memberId, addressId);

		return ResponseEntity.ok().build();
	}

	@PutMapping("/members/{memberId}/addresses/{addressId}")
	public ResponseEntity<Void> updateAddress(
		@PathVariable Long memberId,
		@PathVariable Long addressId,
		@Valid @RequestBody AddressUpdateRequest request
	) {
		memberService.updateAddress(
			memberId,
			addressId,
			request
		);

		return ResponseEntity.noContent().build();
	}
}
