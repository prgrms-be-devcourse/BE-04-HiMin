package com.prgrms.himin.menu.docs;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
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
import com.prgrms.himin.menu.dto.request.MenuOptionCreateRequest;
import com.prgrms.himin.menu.dto.request.MenuOptionUpdateRequest;
import com.prgrms.himin.menu.dto.response.MenuOptionCreateResponse;
import com.prgrms.himin.setup.request.MenuOptionCreateRequestBuilder;
import com.prgrms.himin.setup.request.MenuOptionUpdateRequestBuilder;
import com.prgrms.himin.setup.response.MenuOptionCreateResponseBuilder;

@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(MenuController.class)
public class MenuOptionDocumentationTest {

	@MockBean
	MenuService menuService;

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("메뉴 옵션을 생성할 수 있다.")
	void createMenuOption() throws Exception {
		// given
		MenuOptionCreateRequest request = MenuOptionCreateRequestBuilder.successBuild();
		String body = objectMapper.writeValueAsString(request);

		MenuOptionCreateResponse response = MenuOptionCreateResponseBuilder.successBuild();
		given(menuService.createMenuOption(anyLong(), anyLong(), anyLong(), any(MenuOptionCreateRequest.class)))
			.willReturn(response);

		// when
		ResultActions resultActions = mvc.perform(post(
				"/api/shops/{shopId}/menus/{menuId}/option-groups/{menuOptionGroupId}/options",
				1L, 1L, 1L
			)
				.content(body)
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print());

		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("create-menu-option",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("shopId").description("가게 ID"),
					parameterWithName("menuId").description("메뉴 ID"),
					parameterWithName("menuOptionGroupId").description("메뉴 옵션 그룹 ID")
				),
				requestFields(
					fieldWithPath("name").type(JsonFieldType.STRING).description("메뉴 옵션 이름"),
					fieldWithPath("price").type(JsonFieldType.NUMBER).description("메뉴 옵션 가격")
				),
				responseFields(
					fieldWithPath("menuOptionId").type(JsonFieldType.NUMBER).description("메뉴 옵션 ID"),
					fieldWithPath("name").type(JsonFieldType.STRING).description("메뉴 옵션 이름"),
					fieldWithPath("price").type(JsonFieldType.NUMBER).description("메뉴 옵션 가격")
				)));
	}

	@Test
	@DisplayName("메뉴 옵션을 수정할 수 있다.")
	void updateMenuOption() throws Exception {
		// given
		MenuOptionUpdateRequest request = MenuOptionUpdateRequestBuilder.successBuild();
		String body = objectMapper.writeValueAsString(request);

		// when
		ResultActions resultActions = mvc.perform(put(
			"/api/shops/{shopId}/menus/{menuId}/option-groups/{menuOptionGroupId}/options/{menuOptionId}",
			1L, 1L, 1L, 1L
		)
			.content(body)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		resultActions.andExpect(status().isNoContent())
			.andDo(document("update-menu-option",
				preprocessRequest(prettyPrint()),
				pathParameters(
					parameterWithName("shopId").description("가게 ID"),
					parameterWithName("menuId").description("메뉴 ID"),
					parameterWithName("menuOptionGroupId").description("메뉴 옵션 그룹 ID"),
					parameterWithName("menuOptionId").description("메뉴 옵션 ID")
				),
				requestFields(
					fieldWithPath("name").type(JsonFieldType.STRING).description("메뉴 옵션 이름"),
					fieldWithPath("price").type(JsonFieldType.NUMBER).description("메뉴 옵션 가격")
				)));

	}

	@Test
	@DisplayName("메뉴 옵션을 삭제할 수 있다.")
	void deleteMenuOption() throws Exception {
		// when
		ResultActions resultActions = mvc.perform(delete(
			"/api/shops/{shopId}/menus/{menuId}/option-groups/{menuOptionGroupId}/options/{menuOptionId}",
			1L, 1L, 1L, 1L
		)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		resultActions
			.andDo(document("delete-menu-option",
				pathParameters(
					parameterWithName("shopId").description("가게 ID"),
					parameterWithName("menuId").description("메뉴 ID"),
					parameterWithName("menuOptionGroupId").description("메뉴 옵션 그룹 ID"),
					parameterWithName("menuOptionId").description("메뉴 옵션 ID")
				)));
	}
}
