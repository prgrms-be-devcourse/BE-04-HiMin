package com.prgrms.himin.member.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;

import com.prgrms.himin.global.error.exception.EntityNotFoundException;
import com.prgrms.himin.global.error.exception.InvalidValueException;
import com.prgrms.himin.member.domain.Address;
import com.prgrms.himin.member.domain.Member;
import com.prgrms.himin.member.domain.MemberRepository;
import com.prgrms.himin.member.dto.request.MemberCreateRequest;
import com.prgrms.himin.member.dto.request.MemberLoginRequest;
import com.prgrms.himin.member.dto.request.MemberUpdateRequest;
import com.prgrms.himin.member.dto.response.AddressResponse;
import com.prgrms.himin.member.dto.response.MemberCreateResponse;
import com.prgrms.himin.member.dto.response.MemberResponse;
import com.prgrms.himin.setup.domain.MemberSetUp;
import com.prgrms.himin.setup.request.MemberCreateRequestBuilder;
import com.prgrms.himin.setup.request.MemberLoginRequestBuilder;
import com.prgrms.himin.setup.request.MemberUpdateRequestBuilder;

@Sql("/truncate.sql")
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

			// when
			MemberCreateResponse actual = memberService.createMember(request);

			// then
			String password = request.password();

			member = request.toEntity(password);

			address = new Address(request.addressAlias(), request.address());

			address.attachTo(member);
			MemberCreateResponse expected = MemberCreateResponse.from(member);

			assertThat(actual).usingRecursiveComparison()
				.ignoringFields("id", "addresses")
				.isEqualTo(expected);

			assertThat(actual.addresses()).usingRecursiveFieldByFieldElementComparatorIgnoringFields("addressId")
				.isEqualTo(expected.addresses());
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
			assertDoesNotThrow(() -> memberService.login(request));
		}

		@DisplayName("로그인 아이디가 잘못되어 실패한다.")
		@Test
		void wrong_login_id_fail_test() {
			// given
			MemberLoginRequest wrongRequest = MemberLoginRequestBuilder
				.failBuild("wrongLoginId", request.password());

			// when & then
			assertThatThrownBy(
				() -> memberService.login(wrongRequest)
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
				() -> memberService.login(wrongRequest)
			)
				.isInstanceOf(InvalidValueException.class);
		}
	}

	@DisplayName("회원을 조회할 수 있다.")
	@Nested
	class GetMember {

		@DisplayName("성공한다.")
		@Test
		void success_test() {
			// given
			member = memberSetUp.saveOne();

			// when
			MemberResponse actual = memberService.getMember(member.getId());

			// then
			List<AddressResponse> addressResponses = member.getAddresses()
				.stream()
				.map(AddressResponse::from)
				.toList();

			MemberResponse expected = MemberResponse.of(member, addressResponses);

			assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		}

		@DisplayName("회원id가 잘못되어 실패한다.")
		@Test
		void wrong_id_fail_test() {
			// given
			Long wrongId = 0L;
			member = memberSetUp.saveOne();

			// when & then
			assertThatThrownBy(
				() -> memberService.getMember(wrongId)
			)
				.isInstanceOf(EntityNotFoundException.class);
		}
	}

	@DisplayName("회원을 업데이트 할 수 있다.")
	@Nested
	class UpdateMember {

		MemberUpdateRequest.Info request;

		@DisplayName("성공한다.")
		@Test
		void success_test() {
			// given
			member = memberSetUp.saveOne();
			request = MemberUpdateRequestBuilder.infoSuccessBuild();

			// when
			memberService.updateMember(member.getId(), request);

			// then
			Member actual = memberRepository.findById(member.getId()).get();

			assertThat(request).usingRecursiveComparison()
				.ignoringFields("password", "addresses")
				.isEqualTo(actual);
			boolean result = passwordEncoder.matches(request.password(), actual.getPassword());
			assertThat(result).isTrue();
		}

		@DisplayName("회원id가 잘못되어 실패한다.")
		@Test
		void wrong_id_fail_test() {
			// given
			Long wrongId = 0L;
			member = memberSetUp.saveOne();
			request = MemberUpdateRequestBuilder.infoSuccessBuild();

			// when & then
			assertThatThrownBy(
				() -> memberService.updateMember(wrongId, request)
			)
				.isInstanceOf(EntityNotFoundException.class);
		}
	}

	@DisplayName("회원을 삭제할 수 있다.")
	@Nested
	class DeleteMember {

		@DisplayName("성공한다.")
		@Test
		void success_test() {
			// given
			member = memberSetUp.saveOne();

			// when
			memberService.deleteMember(member.getId());

			// then
			Optional<Member> actual = memberRepository.findById(member.getId());
			assertThat(actual).isEmpty();
		}

		@DisplayName("회원id가 잘못되어 실패한다.")
		@Test
		void wrong_id_fail_test() {
			// given
			Long wrongId = 0L;
			member = memberSetUp.saveOne();

			// when & then
			assertThatThrownBy(
				() -> memberService.deleteMember(wrongId)
			)
				.isInstanceOf(EntityNotFoundException.class);
		}
	}
}
