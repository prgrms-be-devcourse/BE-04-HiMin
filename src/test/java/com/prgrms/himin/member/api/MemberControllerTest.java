package com.prgrms.himin.member.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.himin.global.error.exception.BusinessException;
import com.prgrms.himin.global.error.exception.EntityNotFoundException;
import com.prgrms.himin.global.error.exception.ErrorCode;
import com.prgrms.himin.member.domain.Grade;
import com.prgrms.himin.member.domain.Member;
import com.prgrms.himin.member.dto.request.MemberCreateRequest;
import com.prgrms.himin.member.dto.request.MemberLoginRequest;
import com.prgrms.himin.setup.domain.MemberSetUp;
import com.prgrms.himin.setup.request.MemberCreateRequestBuilder;
import com.prgrms.himin.setup.request.MemberLoginRequestBuilder;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

	final String BASE_URL = "/api";
	@Autowired
	MockMvc mvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	MemberSetUp memberSetUp;

	@Nested
	@DisplayName("회원 생성을 할수 있다.")
	class CreateMember {

		private final String SIGN_UP_URL = BASE_URL + "/sign-up";

		@DisplayName("성공한다.")
		@Test
		void success_test() throws Exception {
			// given
			MemberCreateRequest request = MemberCreateRequestBuilder.successBuild();
			String body = objectMapper.writeValueAsString(request);

			// when
			ResultActions resultActions = mvc.perform(post(SIGN_UP_URL)
					.content(body)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print());

			// then
			resultActions.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("id").isNotEmpty())
				.andExpect(jsonPath("loginId").value(request.loginId()))
				.andExpect(jsonPath("name").value(request.name()))
				.andExpect(jsonPath("phone").value(request.phone()))
				.andExpect(jsonPath("birthday").value(request.birthday().toString()))
				.andExpect(jsonPath("grade").value(Grade.NEW.toString()))
				.andExpect(jsonPath("addresses[0].address").value(request.address()))
				.andExpect(jsonPath("addresses[0].addressAlias").value(request.addressAlias()));
		}

		@ParameterizedTest
		@MethodSource("provideRequestForErrorValue")
		@DisplayName("실패한다.")
		void fail_test(String wrongLoginId, String expectedMessage) throws Exception {
			// given
			MemberCreateRequest request = MemberCreateRequestBuilder.failBuild(wrongLoginId);
			String body = objectMapper.writeValueAsString(request);

			// when
			ResultActions resultActions = mvc.perform(post(SIGN_UP_URL)
					.content(body)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print());

			// then
			resultActions.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(
					result.getResolvedException().getClass().isAssignableFrom(MethodArgumentNotValidException.class)
				))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("error").value(ErrorCode.INVALID_REQUEST.toString()))
				.andExpect(jsonPath("errors[0].field").value("loginId"))
				.andExpect(jsonPath("errors[0].value").value(wrongLoginId))
				.andExpect(jsonPath("errors[0].reason").value(expectedMessage))
				.andExpect(jsonPath("code").value(ErrorCode.INVALID_REQUEST.getCode()))
				.andExpect(jsonPath("message").value(ErrorCode.INVALID_REQUEST.getMessage()));
		}

		private static Stream<Arguments> provideRequestForErrorValue() {
			return Stream.of(
				Arguments.of("123456789012345678901", "loginId은 최대 20글자 입니다."),
				Arguments.of(null, "loginId가 비어있으면 안됩니다."),
				Arguments.of("", "loginId가 비어있으면 안됩니다."),
				Arguments.of("  ", "loginId가 비어있으면 안됩니다.")
			);
		}
	}

	@Nested
	@DisplayName("회원 로그인을 할 수 있다.")
	class LoginMember {

		private final String SIGN_IN_URL = BASE_URL + "/sign-in";

		@DisplayName("성공한다.")
		@Test
		void success_test() throws Exception {
			// given
			memberSetUp.saveOne();
			MemberLoginRequest request = MemberLoginRequestBuilder.successBuild();
			String body = objectMapper.writeValueAsString(request);

			// when
			ResultActions resultActions = mvc.perform(post(SIGN_IN_URL)
					.content(body)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print());

			// then
			resultActions.andExpect(status().isOk());
		}

		@DisplayName("잘못된 아이디로 인해 실패한다.")
		@Test
		void fail_test() throws Exception {
			// given
			memberSetUp.saveOne();
			MemberLoginRequest request = MemberLoginRequestBuilder.failBuild(
				"wrong" + "rnqjaah1234",
				"1234"
			);
			String body = objectMapper.writeValueAsString(request);

			// when
			ResultActions resultActions = mvc.perform(post(SIGN_IN_URL)
					.content(body)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print());

			// then
			resultActions.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(
					result.getResolvedException().getClass().isAssignableFrom(BusinessException.class)
				))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("error").value(ErrorCode.MEMBER_LOGIN_FAIL.toString()))
				.andExpect(jsonPath("code").value(ErrorCode.MEMBER_LOGIN_FAIL.getCode()))
				.andExpect(jsonPath("message").value(ErrorCode.MEMBER_LOGIN_FAIL.getMessage()));
		}
	}

	@Nested
	@DisplayName("회원을 조회할 수 있다.")
	class GetMember {

		private final String GET_MEMBER_URL = BASE_URL + "/members/{memberId}";

		@DisplayName("성공한다.")
		@Test
		void success_test() throws Exception {
			// given
			Member savedMember = memberSetUp.saveOne();

			// when
			ResultActions resultActions = mvc.perform(get(
				GET_MEMBER_URL,
				savedMember.getId())
			);

			// then
			resultActions.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("id").value(savedMember.getId()))
				.andExpect(jsonPath("loginId").value(savedMember.getLoginId()))
				.andExpect(jsonPath("name").value(savedMember.getName()))
				.andExpect(jsonPath("phone").value(savedMember.getPhone()))
				.andExpect(jsonPath("birthday").value(savedMember.getBirthday().toString()))
				.andExpect(jsonPath("grade").value(Grade.NEW.toString()))
				.andExpect(jsonPath("addresses[0].addressAlias").value(savedMember.getAddresses()
					.get(0).getAddressAlias()))
				.andExpect(jsonPath("addresses[0].address").value(savedMember.getAddresses()
					.get(0).getAddress()));

		}

		@DisplayName("잘못된 memberId 조회로 실패한다.")
		@Test
		void wrong_member_id_fail_test() throws Exception {
			// given
			memberSetUp.saveOne();

			// when
			ResultActions resultActions = mvc.perform(get(
				GET_MEMBER_URL,
				-1)
			);

			// then
			resultActions.andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(
					result.getResolvedException().getClass().isAssignableFrom(EntityNotFoundException.class)
				))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("error").value(ErrorCode.MEMBER_NOT_FOUND.toString()))
				.andExpect(jsonPath("code").value(ErrorCode.MEMBER_NOT_FOUND.getCode()))
				.andExpect(jsonPath("message").value(ErrorCode.MEMBER_NOT_FOUND.getMessage()));
		}
	}
	
}
