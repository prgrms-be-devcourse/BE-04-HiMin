package com.prgrms.himin.member.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.prgrms.himin.member.domain.Member;
import com.prgrms.himin.member.domain.MemberRepository;
import com.prgrms.himin.member.dto.request.MemberCreateRequest;
import com.prgrms.himin.member.dto.response.MemberCreateResponse;
import com.prgrms.himin.setup.request.MemberCreateRequestBuilder;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

	@Mock
	MemberRepository memberRepository;

	@InjectMocks
	MemberService memberService;

	@Mock
	PasswordEncoder passwordEncoder;

	Member member;

	MemberCreateRequest request;

	@BeforeEach
	void setup() {
		request = MemberCreateRequestBuilder.successBuild();

		given(passwordEncoder.encode(request.password()))
			.willReturn(request.password());

		String password = passwordEncoder.encode(request.password());
		member = request.toEntity(password);
	}

	@DisplayName("회원 생성을 할 수 있다.")
	@Nested
	class CreateMember {

		@DisplayName("성공한다.")
		@Test
		void success_test() {
			// given
			given(memberRepository.save(any(Member.class))).willReturn(member);

			// when
			MemberCreateResponse actual = memberService.createMember(request);

			// then
			MemberCreateResponse expected = MemberCreateResponse.from(member);
			assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		}
	}
}
