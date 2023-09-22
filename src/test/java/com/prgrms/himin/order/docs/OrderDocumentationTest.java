package com.prgrms.himin.order.docs;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.himin.order.api.OrderController;
import com.prgrms.himin.order.application.OrderService;
import com.prgrms.himin.order.domain.OrderStatus;
import com.prgrms.himin.order.dto.request.OrderCreateRequest;
import com.prgrms.himin.order.dto.request.OrderSearchCondition;
import com.prgrms.himin.order.dto.request.SelectedMenuOptionRequest;
import com.prgrms.himin.order.dto.request.SelectedMenuRequest;
import com.prgrms.himin.order.dto.response.OrderResponse;
import com.prgrms.himin.order.dto.response.OrderResponses;
import com.prgrms.himin.setup.request.OrderCreateRequestBuilder;
import com.prgrms.himin.setup.request.SelectedMenuRequestBuilder;
import com.prgrms.himin.setup.response.OrderResponseBuilder;
import com.prgrms.himin.setup.response.OrderResponsesBuilder;
import com.prgrms.himin.shop.domain.Category;

@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(OrderController.class)
class OrderDocumentationTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	OrderService orderService;

	@Test
	@DisplayName("주문을 생성할 수 있다.")
	void createOrder() throws Exception {
		// given
		SelectedMenuOptionRequest selectedMenuOptionRequest =
			new SelectedMenuOptionRequest(1L, List.of(1L, 2L, 3L));
		SelectedMenuRequest selectedMenuRequest = SelectedMenuRequestBuilder
			.successBuild(1L, List.of(selectedMenuOptionRequest));
		OrderCreateRequest request = OrderCreateRequestBuilder
			.successBuild(1L, 1L, List.of(selectedMenuRequest));

		OrderResponse response = OrderResponseBuilder.successBuild();

		given(orderService.createOrder(any(OrderCreateRequest.class))).willReturn(response);

		// when
		ResultActions resultAction = mvc.perform(post("/api/orders")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request))
		);

		// then
		resultAction.andExpect(status().isOk())
			.andDo(document("order-create",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("회원 ID"),
					fieldWithPath("shopId").type(JsonFieldType.NUMBER).description("가게 ID"),
					fieldWithPath("address").type(JsonFieldType.STRING).description("주소"),
					fieldWithPath("requirement").type(JsonFieldType.STRING).description("요청사항"),
					fieldWithPath("selectedMenus[].menuId").type(JsonFieldType.NUMBER).description("메뉴 ID"),
					fieldWithPath("selectedMenus[].quantity").type(JsonFieldType.NUMBER).description("수량"),
					fieldWithPath("selectedMenus[].selectedMenuOptions[].menuOptionGroupId")
						.type(JsonFieldType.NUMBER).description("메뉴옵션그룹 ID"),
					fieldWithPath("selectedMenus[].selectedMenuOptions[].selectedMenuOptions[]")
						.type(JsonFieldType.ARRAY).description("메뉴옵션")
				),
				responseFields(
					fieldWithPath("orderId").type(JsonFieldType.NUMBER).description("주문 ID"),
					fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("회원 ID"),
					fieldWithPath("shopId").type(JsonFieldType.NUMBER).description("가게 ID"),
					fieldWithPath("address").type(JsonFieldType.STRING).description("주소"),
					fieldWithPath("requirement").type(JsonFieldType.STRING).description("요청사항"),
					fieldWithPath("selectedMenus[].menuId").type(JsonFieldType.NUMBER).description("메뉴 ID"),
					fieldWithPath("selectedMenus[].quantity").type(JsonFieldType.NUMBER).description("수량"),
					fieldWithPath("selectedMenus[].selectedOptionIds[]").type(JsonFieldType.ARRAY).description("메뉴옵션"),
					fieldWithPath("price").type(JsonFieldType.NUMBER).description("가격")
				)));
	}

	@Test
	@DisplayName("주문 조회를 할 수 있다.")
	void getOrder() throws Exception {
		// given
		OrderResponse response = OrderResponseBuilder.successBuild();
		given(orderService.getOrder(anyLong())).willReturn(response);

		// when
		ResultActions resultActions = mvc.perform(RestDocumentationRequestBuilders.get("/api/orders/{orderId}", 1L));

		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("order-get",
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("orderId").description("주문 ID")
				),
				responseFields(
					fieldWithPath("orderId").type(JsonFieldType.NUMBER).description("주문 ID"),
					fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("회원 ID"),
					fieldWithPath("shopId").type(JsonFieldType.NUMBER).description("가게 ID"),
					fieldWithPath("address").type(JsonFieldType.STRING).description("배달 도착 주소"),
					fieldWithPath("requirement").type(JsonFieldType.STRING).description("요구사항"),
					fieldWithPath("selectedMenus[].menuId").type(JsonFieldType.NUMBER).description("선택 메뉴 ID"),
					fieldWithPath("selectedMenus[].quantity").type(JsonFieldType.NUMBER).description("선택 메뉴 수량"),
					fieldWithPath("selectedMenus[].selectedOptionIds[]").type(JsonFieldType.ARRAY)
						.description("선택 메뉴 옵션 ID 목록"),
					fieldWithPath("price").type(JsonFieldType.NUMBER).description("선택 메뉴 총 가격")
				)));
	}

	@DisplayName("주문 다건조회를 할 수 있다.")
	@Test
	void getOrders() throws Exception {
		// given
		OrderResponses responses = OrderResponsesBuilder.successBuild();
		given(orderService.getOrders(anyLong(), any(OrderSearchCondition.class), anyInt(), isNotNull()))
			.willReturn(responses);

		// when
		ResultActions resultAction = mvc.perform(get("/api/orders/list")
			.accept(MediaType.APPLICATION_JSON)
			.param("categories", Category.CAFE.name())
			.param("orderStatuses", OrderStatus.DELIVERED.name())
			.param("startTime",
				"2023-08-21T00:00")
			.param("endTime",
				"2023-08-21T00:00")
			.param("size", "10")
			.param("cursor", "0")
			.header("memberId", 1L)
		);

		// then
		resultAction.andExpect(status().isOk())
			.andDo(document("order-get-by-order-condition",
				preprocessResponse(prettyPrint()),
				requestHeaders(
					headerWithName("memberId").description("회원 ID")
				),
				requestParameters(
					parameterWithName("categories").description("검색 조건 - 카테고리"),
					parameterWithName("orderStatuses").description("검색 조건 - 주문 상태"),
					parameterWithName("startTime").description("검색 조건 - 시작 날짜"),
					parameterWithName("endTime").description("검색 조건 - 끝나는 날짜"),
					parameterWithName("size").description("Pagination - 사이즈"),
					parameterWithName("cursor").description("Pagination - 커서 ID")
				),
				responseFields(
					fieldWithPath("orderResponses[].orderId").type(JsonFieldType.NUMBER).description("주문 ID"),
					fieldWithPath("orderResponses[].memberId").type(JsonFieldType.NUMBER).description("회원 ID"),
					fieldWithPath("orderResponses[].shopId").type(JsonFieldType.NUMBER).description("가게 ID"),
					fieldWithPath("orderResponses[].address").type(JsonFieldType.STRING).description("주소"),
					fieldWithPath("orderResponses[].requirement").type(JsonFieldType.STRING).description("요청사항"),
					fieldWithPath("orderResponses[].selectedMenus[].menuId").type(JsonFieldType.NUMBER)
						.description("선택메뉴 ID"),
					fieldWithPath("orderResponses[].selectedMenus[].quantity").type(JsonFieldType.NUMBER)
						.description("주문수량"),
					fieldWithPath("orderResponses[].selectedMenus[].selectedOptionIds[]").type(JsonFieldType.ARRAY)
						.description("선택옵션 ID"),
					fieldWithPath("orderResponses[].price").type(JsonFieldType.NUMBER)
						.description("가격"),
					fieldWithPath("size").type(JsonFieldType.NUMBER).description("조회 페이지 사이즈"),
					fieldWithPath("nextCursor").type(JsonFieldType.NULL).description("다음 커서ID"),
					fieldWithPath("isLast").type(JsonFieldType.BOOLEAN).description("마지막 페이지 유무")
				)));
	}
}
