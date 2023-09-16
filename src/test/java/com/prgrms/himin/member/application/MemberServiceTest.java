package com.prgrms.himin.member.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.prgrms.himin.global.error.exception.InvalidValueException;
import com.prgrms.himin.member.domain.Address;
import com.prgrms.himin.member.domain.Member;
import com.prgrms.himin.member.domain.MemberRepository;
import com.prgrms.himin.member.dto.request.MemberCreateRequest;
import com.prgrms.himin.member.dto.request.MemberLoginRequest;
import com.prgrms.himin.member.dto.response.MemberCreateResponse;
import com.prgrms.himin.setup.domain.MemberSetUp;
import com.prgrms.himin.setup.request.MemberCreateRequestBuilder;
import com.prgrms.himin.setup.request.MemberLoginRequestBuilder;

@SpringBootTest
class MemberServiceTest {

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	MemberService memberService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	MemberSetUp memberSetUp;

	Member member;

	Address address;

	@DisplayName("회원 생성을 할 수 있다.")
	@Nested
	class CreateMember {

		@DisplayName("성공한다.")
		@Test
		void success_test() {
			// given
			MemberCreateRequest request = MemberCreateRequestBuilder.successBuild();
			String password = request.password();

			member = request.toEntity(password);

			address = new Address(request.addressAlias(), request.address());

			address.attachTo(member);

			// when
			MemberCreateResponse actual = memberService.createMember(request);

			// then
			MemberCreateResponse expected = MemberCreateResponse.from(member);
			assertThat(actual).usingRecursiveComparison()
				.ignoringFields("id", "addresses")
				.isEqualTo(expected);

			assertThat(actual.addresses()).usingRecursiveFieldByFieldElementComparatorIgnoringFields("addressId")
				.isEqualTo(expected.addresses());
			Optional<Member> member = memberRepository.findById(0L);
			assertThat(member).isEmpty();
		}
	}

	@DisplayName("회원 로그인을 할 수 있다.")
	@Nested
	class LoginMember {

		MemberLoginRequest request = MemberLoginRequestBuilder.successBuild();

		@DisplayName("성공한다.")
		@Test
		void success_test() {
			// given
			member = memberSetUp.saveOne();

			// when & then
			assertDoesNotThrow(() -> memberService.login(request.loginId(), request.password()));
		}

		@DisplayName("로그인 아이디가 잘못되어 실패한다.")
		@Test
		void wrong_login_id_fail_test() {
			// given
			MemberLoginRequest wrongRequest = MemberLoginRequestBuilder.failBuild(
				"wrongLoginId",
				request.password()
			);

			// when & then
			assertThatThrownBy(
				() -> memberService.login(wrongRequest.loginId(), wrongRequest.password())
			)
				.isInstanceOf(InvalidValueException.class);
		}

		@DisplayName("비밀번호가 잘못되어 실패한다.")
		@Test
		void wrong_password_fail_test() {
			// given
			MemberLoginRequest wrongRequest = MemberLoginRequestBuilder.failBuild(
				request.loginId(),
				"wrongPassword"
			);

			// when & then
			assertThatThrownBy(
				() -> memberService.login(wrongRequest.loginId(), wrongRequest.password())
			)
				.isInstanceOf(InvalidValueException.class);
		}
	}
}
