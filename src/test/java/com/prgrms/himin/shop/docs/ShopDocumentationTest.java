package com.prgrms.himin.shop.docs;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import com.prgrms.himin.setup.request.ShopCreateRequestBuilder;
import com.prgrms.himin.setup.request.ShopUpdateRequestBuilder;
import com.prgrms.himin.setup.response.ShopResponseBuilder;
import com.prgrms.himin.shop.api.ShopController;
import com.prgrms.himin.shop.application.ShopService;
import com.prgrms.himin.shop.dto.request.ShopCreateRequest;
import com.prgrms.himin.shop.dto.request.ShopUpdateRequest;
import com.prgrms.himin.shop.dto.response.ShopResponse;

@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ShopController.class)
class ShopDocumentationTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ShopService shopService;

	@DisplayName("가게를 생성할 수 있다.")
	@Test
	void createShop() throws Exception {
		// given
		ShopCreateRequest request = ShopCreateRequestBuilder.successBuild();
		ShopResponse response = ShopResponseBuilder.successBuild();

		given(shopService.createShop(any(ShopCreateRequest.class))).willReturn(response);

		// when
		ResultActions resultAction = mvc.perform(post("/api/shops")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request))
		);

		// then
		resultAction.andExpect(status().isOk())
			.andDo(document("create-shop",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("name").type(JsonFieldType.STRING).description("가게 이름"),
					fieldWithPath("category").type(JsonFieldType.STRING).description("음식 카테고리"),
					fieldWithPath("address").type(JsonFieldType.STRING).description("주소"),
					fieldWithPath("phone").type(JsonFieldType.STRING).description("전화번호"),
					fieldWithPath("content").type(JsonFieldType.STRING).description("소개글"),
					fieldWithPath("deliveryTip").type(JsonFieldType.NUMBER).description("배달팁"),
					fieldWithPath("openingTime").type(JsonFieldType.STRING).description("오픈 시간"),
					fieldWithPath("closingTime").type(JsonFieldType.STRING).description("폐점 시간")
				),
				responseFields(
					fieldWithPath("shopId").type(JsonFieldType.NUMBER).description("가게 ID"),
					fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
					fieldWithPath("category").type(JsonFieldType.STRING).description("음식 카테고리"),
					fieldWithPath("address").type(JsonFieldType.STRING).description("주소"),
					fieldWithPath("phone").type(JsonFieldType.STRING).description("전화번호"),
					fieldWithPath("content").type(JsonFieldType.STRING).description("소개글"),
					fieldWithPath("deliveryTip").type(JsonFieldType.NUMBER).description("배달팁"),
					fieldWithPath("dibsCount").type(JsonFieldType.NUMBER).description("찜 수"),
					fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
					fieldWithPath("openingTime").type(JsonFieldType.STRING).description("오픈 시간"),
					fieldWithPath("closingTime").type(JsonFieldType.STRING).description("폐점 시간")
				)));
	}

	@Test
	@DisplayName("가게를 조회할 수 있다.")
	void getShop() throws Exception {
		// given
		ShopResponse response = ShopResponseBuilder.successBuild();

		given(shopService.getShop(anyLong())).willReturn(response);

		// when
		ResultActions resultAction = mvc.perform(get("/api/shops/{shopId}", 1L));

		// then
		resultAction.andExpect(status().isOk())
			.andDo(document("get-shop",
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("shopId").description("가게 ID")
				),
				responseFields(
					fieldWithPath("shopId").type(JsonFieldType.NUMBER).description("가게 ID"),
					fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
					fieldWithPath("category").type(JsonFieldType.STRING).description("음식 카테고리"),
					fieldWithPath("address").type(JsonFieldType.STRING).description("주소"),
					fieldWithPath("phone").type(JsonFieldType.STRING).description("전화번호"),
					fieldWithPath("content").type(JsonFieldType.STRING).description("소개글"),
					fieldWithPath("deliveryTip").type(JsonFieldType.NUMBER).description("배달팁"),
					fieldWithPath("dibsCount").type(JsonFieldType.NUMBER).description("찜 수"),
					fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
					fieldWithPath("openingTime").type(JsonFieldType.STRING).description("오픈 시간"),
					fieldWithPath("closingTime").type(JsonFieldType.STRING).description("폐점 시간")
				)));
	}

	@DisplayName("가게 정보를 수정할 수 있다.")
	@Test
	void updateShop() throws Exception {
		// given
		ShopUpdateRequest.Info request = ShopUpdateRequestBuilder.infoSuccessBuild();

		willDoNothing().given(shopService).updateShop(anyLong(), any(ShopUpdateRequest.Info.class));

		// when
		ResultActions resultAction = mvc.perform(put("/api/shops/{shopId}", 1L)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request))
		);

		// then
		resultAction.andExpect(status().isNoContent())
			.andDo(document("update-shop",
				preprocessRequest(prettyPrint()),
				requestFields(
					fieldWithPath("name").type(JsonFieldType.STRING).description("가게 이름"),
					fieldWithPath("category").type(JsonFieldType.STRING).description("음식 카테고리"),
					fieldWithPath("address").type(JsonFieldType.STRING).description("주소"),
					fieldWithPath("phone").type(JsonFieldType.STRING).description("전화번호"),
					fieldWithPath("content").type(JsonFieldType.STRING).description("소개글"),
					fieldWithPath("deliveryTip").type(JsonFieldType.NUMBER).description("배달팁"),
					fieldWithPath("openingTime").type(JsonFieldType.STRING).description("오픈 시간"),
					fieldWithPath("closingTime").type(JsonFieldType.STRING).description("폐점 시간")
				)));
	}

	@DisplayName("가게 상태를 변경할 수 있다.")
	@Test
	void changeShopStatus() throws Exception {
		// given
		ShopUpdateRequest.Status request = ShopUpdateRequestBuilder.statusSuccessBuild();

		willDoNothing().given(shopService).changeShopStatus(anyLong(), any(ShopUpdateRequest.Status.class));

		// when
		ResultActions resultAction = mvc.perform(patch("/api/shops/{shopId}", 1L)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request))
		);

		// then
		resultAction.andExpect(status().isNoContent())
			.andDo(document("change-shop-status",
				preprocessRequest(prettyPrint()),
				requestFields(
					fieldWithPath("status").type(JsonFieldType.STRING).description("상태")
				)));
	}
}
