package com.prgrms.himin.shop.docs;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
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
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.himin.setup.request.ShopCreateRequestBuilder;
import com.prgrms.himin.setup.request.ShopUpdateRequestBuilder;
import com.prgrms.himin.setup.response.ShopResponseBuilder;
import com.prgrms.himin.setup.response.ShopsResponseBuilder;
import com.prgrms.himin.shop.api.ShopController;
import com.prgrms.himin.shop.application.ShopService;
import com.prgrms.himin.shop.domain.ShopSort;
import com.prgrms.himin.shop.dto.request.ShopCreateRequest;
import com.prgrms.himin.shop.dto.request.ShopSearchCondition;
import com.prgrms.himin.shop.dto.request.ShopUpdateRequest;
import com.prgrms.himin.shop.dto.response.ShopResponse;
import com.prgrms.himin.shop.dto.response.ShopsResponse;

@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ShopController.class)
class ShopDocumentationTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	WebApplicationContext webApplicationContext;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	ShopService shopService;

	@Test
	@DisplayName("가게를 생성할 수 있다.")
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
			.andDo(document("shop-create",
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
			.andDo(document("shop-get-one",
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

	@Test
	@DisplayName("가게 목록을 조회할 수 있다.")
	void getShops() throws Exception {
		// given
		ShopsResponse responses = ShopsResponseBuilder.successBuild();

		given(shopService.getShops(any(ShopSearchCondition.class), anyInt(), anyLong(), any(ShopSort.class)))
			.willReturn(responses);

		// when
		ResultActions resultAction = mvc.perform(
			get("/api/shops")
				.param("name", "맥도날드")
				.param("category", "CAFE")
				.param("address", "광명")
				.param("deliveryTip", "4000")
				.param("menuName", "햄버거")
				.param("size", "3")
				.param("cursor", "1")
				.param("sort", "deliveryTipAsc")
		);

		// then
		resultAction.andExpect(status().isOk())
			.andDo(document("shop-get-many",
				preprocessResponse(prettyPrint()),
				requestParameters(
					parameterWithName("name").description("검색 조건 - 이름"),
					parameterWithName("category").description("검색 조건 - 카테고리"),
					parameterWithName("address").description("검색 조건 - 주소"),
					parameterWithName("deliveryTip").description("검색 조건 - 배탈팁"),
					parameterWithName("menuName").description("검색 조건 - 메뉴 이름"),
					parameterWithName("size").description("Pagination - 사이즈"),
					parameterWithName("cursor").description("Pagination - 커서 ID"),
					parameterWithName("sort").description("정렬 조건")
				),
				responseFields(
					fieldWithPath("shopResponses[].shopId").type(JsonFieldType.NUMBER).description("가게 ID"),
					fieldWithPath("shopResponses[].name").type(JsonFieldType.STRING).description("이름"),
					fieldWithPath("shopResponses[].category").type(JsonFieldType.STRING).description("음식 카테고리"),
					fieldWithPath("shopResponses[].address").type(JsonFieldType.STRING).description("주소"),
					fieldWithPath("shopResponses[].phone").type(JsonFieldType.STRING).description("전화번호"),
					fieldWithPath("shopResponses[].content").type(JsonFieldType.STRING).description("소개글"),
					fieldWithPath("shopResponses[].deliveryTip").type(JsonFieldType.NUMBER).description("배달팁"),
					fieldWithPath("shopResponses[].dibsCount").type(JsonFieldType.NUMBER).description("찜 수"),
					fieldWithPath("shopResponses[].status").type(JsonFieldType.STRING).description("상태"),
					fieldWithPath("shopResponses[].openingTime").type(JsonFieldType.STRING).description("오픈 시간"),
					fieldWithPath("shopResponses[].closingTime").type(JsonFieldType.STRING).description("폐점 시간"),
					fieldWithPath("size").type(JsonFieldType.NUMBER).description("페이지 사이즈"),
					fieldWithPath("nextCursor").type(JsonFieldType.NUMBER).description("다음 커서 ID"),
					fieldWithPath("sort").type(JsonFieldType.STRING).description("정렬 조건"),
					fieldWithPath("isLast").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부")
				)));
	}

	@Test
	@DisplayName("가게 정보를 수정할 수 있다.")
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
			.andDo(document("shop-update-info",
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

	@Test
	@DisplayName("가게 상태를 변경할 수 있다.")
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
			.andDo(document("shop-change-status",
				preprocessRequest(prettyPrint()),
				requestFields(
					fieldWithPath("status").type(JsonFieldType.STRING).description("상태")
				)));
	}

	@Test
	@DisplayName("가게를 삭제할 수 있다.")
	void deleteShop() throws Exception {
		// given
		willDoNothing().given(shopService).deleteShop(anyLong());

		// when
		ResultActions resultAction = mvc.perform(delete("/api/shops/{shopId}", 1L));

		// then
		resultAction.andExpect(status().isOk())
			.andDo(document("shop-delete",
				pathParameters(
					parameterWithName("shopId").description("가게 ID")
				)));
	}

	@DisplayName("조리를 시작할 수 있다.")
	@Test
	void startCooking() throws Exception {
		// given
		willDoNothing().given(shopService).startCooking(anyLong(), anyLong());

		// when
		ResultActions resultAction = mvc.perform(
			post("/api/shops/{shopId}/cook-beginning/{orderId}", 1L, 1L)
		);

		// then
		resultAction.andExpect(status().isOk())
			.andDo(document("shop-start-cooking",
				pathParameters(
					parameterWithName("shopId").description("가게 ID"),
					parameterWithName("orderId").description("주문 ID")
				)));
	}

	@DisplayName("조리를 완료할 수 있다.")
	@Test
	void finishCooking() throws Exception {
		// given
		willDoNothing().given(shopService).finishCooking(anyLong(), anyLong());

		// when
		ResultActions resultAction = mvc.perform(
			post("/api/shops/{shopId}/cook-completion/{orderId}", 1L, 1L)
		);

		// then
		resultAction.andExpect(status().isOk())
			.andDo(document("shop-finish-cooking",
				pathParameters(
					parameterWithName("shopId").description("가게 ID"),
					parameterWithName("orderId").description("주문 ID")
				)));
	}
}
