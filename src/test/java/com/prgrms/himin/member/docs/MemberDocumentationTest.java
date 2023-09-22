package com.prgrms.himin.member.docs;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.himin.member.api.MemberController;
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
import com.prgrms.himin.setup.request.AddressCreateRequestBuilder;
import com.prgrms.himin.setup.request.AddressUpdateRequestBuilder;
import com.prgrms.himin.setup.request.MemberCreateRequestBuilder;
import com.prgrms.himin.setup.request.MemberLoginRequestBuilder;
import com.prgrms.himin.setup.request.MemberUpdateRequestBuilder;
import com.prgrms.himin.setup.response.AddressResponseBuilder;
import com.prgrms.himin.setup.response.MemberLoginResponseBuilder;
import com.prgrms.himin.setup.response.MemberResponseBuilder;

@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(MemberController.class)
class MemberDocumentationTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	MemberService memberService;

	@MockBean
	AuthenticationManager authenticationManager;

	@DisplayName("회원을 생성할 수 있다.")
	@Test
	void createMember() throws Exception {
		// given
		MemberCreateRequest request = MemberCreateRequestBuilder.successBuild();
		MemberCreateResponse response = MemberResponseBuilder.successCreateBuild();

		given(memberService.createMember(any(MemberCreateRequest.class))).willReturn(response);

		// when
		ResultActions resultAction = mvc.perform(post("/api/sign-up")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)));

		// then
		resultAction.andExpect(status().isOk())
			.andDo(document("member-create",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 ID"),
					fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
					fieldWithPath("name").type(JsonFieldType.STRING).description("회원 이름"),
					fieldWithPath("phone").type(JsonFieldType.STRING).description("회원 전화번호"),
					fieldWithPath("birthday").type(JsonFieldType.STRING).description("생일"),
					fieldWithPath("addressAlias").type(JsonFieldType.STRING).description("주소 가명"),
					fieldWithPath("address").type(JsonFieldType.STRING).description("주소")
				),
				responseFields(
					fieldWithPath("id").type(JsonFieldType.NUMBER).description("ID"),
					fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 ID"),
					fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
					fieldWithPath("phone").type(JsonFieldType.STRING).description("전화번호"),
					fieldWithPath("birthday").type(JsonFieldType.STRING).description("생일"),
					fieldWithPath("grade").type(JsonFieldType.STRING).description("등급"),
					fieldWithPath("addresses[0].addressId").type(JsonFieldType.NUMBER).description("주소 ID"),
					fieldWithPath("addresses[0].addressAlias").type(JsonFieldType.STRING).description("주소 가명"),
					fieldWithPath("addresses[0].address").type(JsonFieldType.STRING).description("주소")
				)));
	}

	@Test
	@DisplayName("로그인할 수 있다.")
	void login() throws Exception {
		// given
		MemberLoginRequest request = MemberLoginRequestBuilder.successBuild();
		MemberLoginResponse response = MemberLoginResponseBuilder.successBuild();

		given(memberService.login(any(MemberLoginRequest.class))).willReturn(response);

		// when
		ResultActions resultAction = mvc.perform(post("/api/sign-in")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)));

		// then
		resultAction.andExpect(status().isOk())
			.andDo(document("member-login",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 ID"),
					fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
				),
				responseFields(
					fieldWithPath("token").type(JsonFieldType.STRING).description("JWT 토큰"),
					fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("회원 ID"),
					fieldWithPath("group[]").type(JsonFieldType.ARRAY).description("역할")
				)));
	}

	@DisplayName("회원을 단건 조회할 수 있다.")
	@Test
	void getMember() throws Exception {
		// given
		MemberResponse response = MemberResponseBuilder.successGetBuild();

		given(memberService.getMember(anyLong())).willReturn(response);

		// when
		ResultActions resultAction = mvc.perform(get("/api/members/{memberId}", 1L)
			.accept(MediaType.APPLICATION_JSON));

		// then
		resultAction.andExpect(status().isOk())
			.andDo(document("member-get",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("memberId").description("회원 ID")
				),
				responseFields(
					fieldWithPath("id").type(JsonFieldType.NUMBER).description("ID"),
					fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 ID"),
					fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
					fieldWithPath("phone").type(JsonFieldType.STRING).description("전화번호"),
					fieldWithPath("birthday").type(JsonFieldType.STRING).description("생일"),
					fieldWithPath("grade").type(JsonFieldType.STRING).description("등급"),
					fieldWithPath("addresses[0].addressId").type(JsonFieldType.NUMBER).description("주소 ID"),
					fieldWithPath("addresses[0].addressAlias").type(JsonFieldType.STRING).description("주소 가명"),
					fieldWithPath("addresses[0].address").type(JsonFieldType.STRING).description("주소")
				)));
	}

	@DisplayName("회원을 업데이트 할 수 있다.")
	@Test
	void updateMember() throws Exception {
		// given
		MemberUpdateRequest.Info request = MemberUpdateRequestBuilder.infoSuccessBuild();

		willDoNothing().given(memberService).updateMember(anyLong(), any(MemberUpdateRequest.Info.class));

		// when
		ResultActions resultAction = mvc.perform(put("/api/members/{memberId}", 1L)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)));

		// then
		resultAction.andExpect(status().isNoContent())
			.andDo(document("member-update",
				preprocessRequest(prettyPrint()),
				pathParameters(
					parameterWithName("memberId").description("회원 ID")
				),
				requestFields(
					fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 ID"),
					fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
					fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
					fieldWithPath("phone").type(JsonFieldType.STRING).description("전화번호"),
					fieldWithPath("birthday").type(JsonFieldType.STRING).description("생일")
				)));
	}

	@DisplayName("회원을 삭제할 수 있다.")
	@Test
	void deleteMember() throws Exception {
		// given
		willDoNothing().given(memberService).deleteMember(anyLong());

		// when
		ResultActions resultAction = mvc.perform(delete("/api/withdrawal/{memberId}", 1L));

		// then
		resultAction.andExpect(status().isOk())
			.andDo(document("member-delete",
				pathParameters(
					parameterWithName("memberId").description("회원 ID")
				)));
	}

	@DisplayName("회원의 주소를 추가할 수 있다.")
	@Test
	void createAddress() throws Exception {
		// given
		AddressCreateRequest request = AddressCreateRequestBuilder.successBuild();
		AddressResponse response = AddressResponseBuilder.successBuild();

		given(memberService.createAddress(anyLong(), any(AddressCreateRequest.class))).willReturn(response);

		// when
		ResultActions resultAction = mvc.perform(post("/api/members/{memberId}/addresses", 1L)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request))
			.accept(MediaType.APPLICATION_JSON));

		// then
		resultAction.andExpect(status().isOk())
			.andDo(document("member-address-create",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("memberId").description("회원 ID")
				),
				requestFields(
					fieldWithPath("addressAlias").type(JsonFieldType.STRING).description("주소 가명"),
					fieldWithPath("address").type(JsonFieldType.STRING).description("주소")
				),
				responseFields(
					fieldWithPath("addressId").type(JsonFieldType.NUMBER).description("주소 ID"),
					fieldWithPath("addressAlias").type(JsonFieldType.STRING).description("주소 가명"),
					fieldWithPath("address").type(JsonFieldType.STRING).description("주소")
				)));
	}

	@DisplayName("회원의 모든 주소를 조회할 수 있다.")
	@Test
	void getAllAddresses() throws Exception {
		// given
		List<AddressResponse> responses = AddressResponseBuilder.successListBuild();

		given(memberService.getAllAddress(anyLong())).willReturn(responses);

		// when
		ResultActions resultAction = mvc.perform(get("/api/members/{memberId}/addresses", 1L)
			.accept(MediaType.APPLICATION_JSON));

		// then
		resultAction.andExpect(status().isOk())
			.andDo(document("member-address-get-all",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("memberId").description("회원 ID")
				),
				responseFields(
					fieldWithPath("[0].addressId").type(JsonFieldType.NUMBER).description("주소 ID"),
					fieldWithPath("[0].addressAlias").type(JsonFieldType.STRING).description("주소 가명"),
					fieldWithPath("[0].address").type(JsonFieldType.STRING).description("주소"),
					fieldWithPath("[1].addressId").type(JsonFieldType.NUMBER).description("주소 ID"),
					fieldWithPath("[1].addressAlias").type(JsonFieldType.STRING).description("주소 가명"),
					fieldWithPath("[1].address").type(JsonFieldType.STRING).description("주소")
				)));
	}

	@DisplayName("주소를 삭제할 수 있다.")
	@Test
	void deleteAddress() throws Exception {
		// given
		willDoNothing().given(memberService).deleteAddress(anyLong(), anyLong());

		// when
		ResultActions resultAction = mvc.perform(
			delete("/api/members/{memberId}/addresses/{addressId}", 1L, 1L)
		);

		// then
		resultAction.andExpect(status().isOk())
			.andDo(document("member-address-delete",
				pathParameters(
					parameterWithName("memberId").description("회원 ID"),
					parameterWithName("addressId").description("주소 ID")
				)));
	}

	@DisplayName("주소를 업데이트 할 수 있다.")
	@Test
	void updateAddress() throws Exception {
		// given
		AddressUpdateRequest request = AddressUpdateRequestBuilder.successBuild();

		willDoNothing().given(memberService).updateAddress(
			anyLong(),
			anyLong(),
			any(AddressUpdateRequest.class)
		);

		// when
		ResultActions resultAction = mvc.perform(
			put("/api/members/{memberId}/addresses/{addressId}", 1L, 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)));

		// then
		resultAction.andExpect(status().isNoContent())
			.andDo(document("member-address-update",
				preprocessRequest(prettyPrint()),
				pathParameters(
					parameterWithName("memberId").description("회원 ID"),
					parameterWithName("addressId").description("주소 ID")
				),
				requestFields(
					fieldWithPath("addressAlias").type(JsonFieldType.STRING).description("주소 가명"),
					fieldWithPath("address").type(JsonFieldType.STRING).description("주소")
				)));
	}
}
