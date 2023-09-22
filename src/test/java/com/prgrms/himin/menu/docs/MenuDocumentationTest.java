package com.prgrms.himin.menu.docs;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.himin.menu.api.MenuController;
import com.prgrms.himin.menu.application.MenuService;
import com.prgrms.himin.menu.dto.request.MenuCreateRequest;
import com.prgrms.himin.menu.dto.request.MenuUpdateRequest;
import com.prgrms.himin.menu.dto.response.MenuCreateResponse;
import com.prgrms.himin.menu.dto.response.MenuResponse;
import com.prgrms.himin.setup.request.MenuCreateRequestBuilder;
import com.prgrms.himin.setup.request.MenuUpdateRequestBuilder;
import com.prgrms.himin.setup.response.MenuCreateResponseBuilder;
import com.prgrms.himin.setup.response.MenuResponseBuilder;

@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(MenuController.class)
public class MenuDocumentationTest {

	@MockBean
	MenuService menuService;

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper objectMapper;

	@DisplayName("메뉴를 생성할 수 있다.")
	@Test
	void createMenu() throws Exception {
		// given
		MenuCreateRequest request = MenuCreateRequestBuilder.successBuild();
		MenuCreateResponse response = MenuCreateResponseBuilder.successBuild();
		String body = objectMapper.writeValueAsString(request);

		given(menuService.createMenu(anyLong(), any(MenuCreateRequest.class))).willReturn(response);

		// when
		ResultActions resultActions = mvc.perform(post("/api/shops/{shopId}/menus", 1L)
			.content(body)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("create-menu",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("name").type(JsonFieldType.STRING).description("메뉴 이름"),
					fieldWithPath("price").type(JsonFieldType.NUMBER).description("메뉴 가격"),
					fieldWithPath("popularity").type(JsonFieldType.BOOLEAN).description("메뉴 인기 여부")
				),
				responseFields(
					fieldWithPath("menuId").type(JsonFieldType.NUMBER).description("메뉴 ID"),
					fieldWithPath("name").type(JsonFieldType.STRING).description("메뉴 이름"),
					fieldWithPath("price").type(JsonFieldType.NUMBER).description("메뉴 가격"),
					fieldWithPath("popularity").type(JsonFieldType.BOOLEAN).description("메뉴 인기 여부"),
					fieldWithPath("status").type(JsonFieldType.STRING).description("메뉴 상태")
				)));
	}

	@Test
	@DisplayName("메뉴를 조회할 수 있다.")
	void getMenu() throws Exception {
		// given
		MenuResponse response = MenuResponseBuilder.successBuild();
		given(menuService.getMenu(anyLong(), anyLong())).willReturn(response);

		// when
		ResultActions resultActions = mvc.perform(get("/api/shops/{shopId}/menus/{menuId}", 1L, 1L)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("get-menu",
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("shopId").description("가게 ID"),
					parameterWithName("menuId").description("메뉴 ID")
				),
				responseFields(
					fieldWithPath("menuId").type(JsonFieldType.NUMBER).description("메뉴 ID"),
					fieldWithPath("name").type(JsonFieldType.STRING).description("메뉴 이름"),
					fieldWithPath("price").type(JsonFieldType.NUMBER).description("메뉴 가격"),
					fieldWithPath("popularity").type(JsonFieldType.BOOLEAN).description("메뉴 인기 여부"),
					fieldWithPath("status").type(JsonFieldType.STRING).description("메뉴 상태"),
					fieldWithPath("menuOptionGroupResponses[].menuOptionGroupId")
						.type(JsonFieldType.NUMBER)
						.description("메뉴 옵션 그룹 아이디"),
					fieldWithPath("menuOptionGroupResponses[].name")
						.type(JsonFieldType.STRING)
						.description("메뉴 옵션 그룹 이름"),
					fieldWithPath(
						"menuOptionGroupResponses[].menuOptionResponses[].menuOptionId")
						.type(JsonFieldType.NUMBER)
						.description("메뉴 옵션 ID"),
					fieldWithPath("menuOptionGroupResponses[].menuOptionResponses[].name")
						.type(JsonFieldType.STRING)
						.description("메뉴 옵션 이름"),
					fieldWithPath("menuOptionGroupResponses[].menuOptionResponses[].price")
						.type(JsonFieldType.NUMBER)
						.description("메뉴 옵션 가격")
				)));

	}

	@Test
	@DisplayName("메뉴를 수정할 수 있다.")
	void updateMenu() throws Exception {
		// given
		MenuUpdateRequest.Info request = MenuUpdateRequestBuilder.infoSuccessBuild();
		String body = objectMapper.writeValueAsString(request);

		// when & then
		ResultActions resultActions = mvc.perform(put("/api/shops/{shopId}/menus/{menuId}", 1L, 1L)
			.content(body)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		resultActions.andExpect(status().isNoContent())
			.andDo(document("update-menu",
				pathParameters(
					parameterWithName("shopId").description("가게 ID"),
					parameterWithName("menuId").description("메뉴 ID")
				),
				requestFields(
					fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
					fieldWithPath("price").type(JsonFieldType.NUMBER).description("가격")
				)
			));
	}

	@DisplayName("메뉴 상태를 수정할 수 있다.")
	@Test
	void updateMenuStatus() throws Exception {
		// given
		MenuUpdateRequest.Status request = MenuUpdateRequestBuilder.statusSuccessBuild();
		String body = objectMapper.writeValueAsString(request);

		// when
		ResultActions resultActions = mvc.perform(patch("/api/shops/{shopId}/menus/{menuId}", 1L, 1L)
			.content(body)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		resultActions.andExpect(status().isNoContent())
			.andDo(document("update-menu-status",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("shopId").description("가게 ID"),
					parameterWithName("menuId").description("메뉴 ID")
				),
				requestFields(
					fieldWithPath("status").description("상태")
				)
			));
	}

	@Test
	@DisplayName("메뉴를 삭제할 수 있다.")
	void deleteMenu() throws Exception {
		// when
		ResultActions resultActions = mvc.perform(delete("/api/shops/{shopId}/menus/{menuId}", 1L, 1L)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("delete-menu",
				pathParameters(
					parameterWithName("shopId").description("가게 ID"),
					parameterWithName("menuId").description("메뉴 ID")
				)));
	}
}
