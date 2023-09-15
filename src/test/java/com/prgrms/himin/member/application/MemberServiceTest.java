package com.prgrms.himin.member.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.prgrms.himin.member.domain.Address;
import com.prgrms.himin.member.domain.Member;
import com.prgrms.himin.member.domain.MemberRepository;
import com.prgrms.himin.member.dto.request.MemberCreateRequest;
import com.prgrms.himin.member.dto.response.MemberCreateResponse;
import com.prgrms.himin.setup.request.MemberCreateRequestBuilder;

@SpringBootTest
class MemberServiceTest {

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	MemberService memberService;

	@Autowired
	PasswordEncoder passwordEncoder;

	Member member;

	Address address;

	MemberCreateRequest request;

	@BeforeEach
	void setup() {
		request = MemberCreateRequestBuilder.successBuild();
	}

	@DisplayName("회원 생성을 할 수 있다.")
	@Nested
	class CreateMember {

		@DisplayName("성공한다.")
		@Test
		void success_test() {
			// given
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
		}
	}
}
