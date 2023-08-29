package com.prgrms.himin.member.api;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.himin.member.application.MemberService;
import com.prgrms.himin.member.dto.request.MemberCreateRequest;
import com.prgrms.himin.member.dto.response.MemberCreateResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/sign-up")
	public ResponseEntity<MemberCreateResponse> createMember(@Valid @RequestBody MemberCreateRequest request) {
		return ResponseEntity.ok(memberService.createMember(request));
	}
}
