package com.prgrms.himin.order.docs;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.himin.order.api.OrderController;
import com.prgrms.himin.order.application.OrderService;
import com.prgrms.himin.order.dto.request.OrderCreateRequest;
import com.prgrms.himin.order.dto.request.SelectedMenuOptionRequest;
import com.prgrms.himin.order.dto.request.SelectedMenuRequest;
import com.prgrms.himin.order.dto.response.OrderResponse;
import com.prgrms.himin.setup.request.OrderCreateRequestBuilder;
import com.prgrms.himin.setup.request.SelectedMenuRequestBuilder;
import com.prgrms.himin.setup.response.OrderResponseBuilder;

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
		SelectedMenuOptionRequest selectedMenuOptionRequest = new SelectedMenuOptionRequest(1L, List.of(1L, 2L, 3L));
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
}
