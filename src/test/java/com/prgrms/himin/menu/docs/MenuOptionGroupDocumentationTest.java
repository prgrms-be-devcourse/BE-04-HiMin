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
import com.prgrms.himin.menu.dto.request.MenuOptionGroupCreateRequest;
import com.prgrms.himin.menu.dto.request.MenuOptionGroupUpdateRequest;
import com.prgrms.himin.menu.dto.response.MenuOptionGroupCreateResponse;
import com.prgrms.himin.setup.request.MenuOptionGroupRequestBuilder;
import com.prgrms.himin.setup.request.MenuOptionGroupUpdateRequestBuilder;
import com.prgrms.himin.setup.response.MenuOptionGroupCreateResponseBuilder;

@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(MenuController.class)
public class MenuOptionGroupDocumentationTest {

	@MockBean
	MenuService menuService;

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("메뉴 옵션 그룹을 생성할 수 있다.")
	void createMenuOptionGroup() throws Exception {
		// given
		MenuOptionGroupCreateRequest request = MenuOptionGroupRequestBuilder.successBuild();
		String body = objectMapper.writeValueAsString(request);

		MenuOptionGroupCreateResponse response = MenuOptionGroupCreateResponseBuilder.successBuild();

		given(menuService.createMenuOptionGroup(anyLong(), anyLong(), any(MenuOptionGroupCreateRequest.class)))
			.willReturn(response);

		// when
		ResultActions resultActions = mvc.perform(post("/api/shops/{shopId}/menus/{menuId}/option-groups", 1L, 1L)
			.content(body)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("create-menu-option-group",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("shopId").description("가게 ID"),
					parameterWithName("menuId").description("메뉴 ID")
				),
				requestFields(
					fieldWithPath("name").type(JsonFieldType.STRING).description("메뉴 옵션 그룹 이름")
				),
				responseFields(
					fieldWithPath("menuOptionGroupId").type(JsonFieldType.NUMBER).description("메뉴 옵션 그룹 ID"),
					fieldWithPath("name").type(JsonFieldType.STRING).description("메뉴 옵션 그룹 이름")
				)));
	}

	@Test
	@DisplayName("메뉴 옵션 그룹을 수정할 수 있다.")
	void updateMenuOptionGroup() throws Exception {
		// given
		MenuOptionGroupUpdateRequest request = MenuOptionGroupUpdateRequestBuilder.successBuild();
		String body = objectMapper.writeValueAsString(request);

		// when
		ResultActions resultActions = mvc.perform(
			put("/api/shops/{shopId}/menus/{menuId}/option-groups/{menuOptionGroupId}",
				1L, 1L, 1L)
				.content(body)
				.contentType(MediaType.APPLICATION_JSON));

		// then
		resultActions.andExpect(status().isNoContent())
			.andDo(document("update-menu-option-group",
				preprocessRequest(prettyPrint()),
				pathParameters(
					parameterWithName("shopId").description("가게 ID"),
					parameterWithName("menuId").description("메뉴 ID"),
					parameterWithName("menuOptionGroupId").description("메뉴 옵션 그룹 ID")
				),
				requestFields(
					fieldWithPath("name").type(JsonFieldType.STRING).description("메뉴 옵션 그룹 이름")
				)));
	}

	@Test
	@DisplayName("메뉴 옵션 그룹을 삭제할 수 있다.")
	void deleteMenuOptionGroup() throws Exception {
		// when
		ResultActions resultActions = mvc.perform(
			delete("/api/shops/{shopId}/menus/{menuId}/option-groups/{menuOptionGroupId}", 1L, 1L, 1L));

		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("delete-menu-option-group",
				pathParameters(
					parameterWithName("shopId").description("가게 ID"),
					parameterWithName("menuId").description("메뉴 ID"),
					parameterWithName("menuOptionGroupId").description("메뉴 옵션 그룹 ID")
				)));

	}
}
