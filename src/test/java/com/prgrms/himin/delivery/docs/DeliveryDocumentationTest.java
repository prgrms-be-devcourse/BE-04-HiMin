package com.prgrms.himin.delivery.docs;

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

import com.prgrms.himin.delivery.api.DeliveryController;
import com.prgrms.himin.delivery.application.DeliveryService;
import com.prgrms.himin.delivery.dto.response.DeliveryHistoryResponse;
import com.prgrms.himin.delivery.dto.response.DeliveryResponse;
import com.prgrms.himin.setup.response.DeliveryHistoryResponseBuilder;
import com.prgrms.himin.setup.response.DeliveryResponseBuilder;

@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(DeliveryController.class)
class DeliveryDocumentationTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private DeliveryService deliveryService;

	@DisplayName("배달을 생성할 수 있다.")
	@Test
	void createDelivery() throws Exception {
		// given
		DeliveryResponse response = DeliveryResponseBuilder.beforeDeliveryBuild();

		given(deliveryService.createDelivery(anyLong())).willReturn(response);

		// when
		ResultActions resultAction = mvc.perform(post("/api/deliveries/{orderId}", 1L)
			.accept(MediaType.APPLICATION_JSON)
		);

		// then
		resultAction.andExpect(status().isOk())
			.andDo(document("delivery-create",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("orderId").description("주문 ID")
				),
				responseFields(
					fieldWithPath("deliveryId").type(JsonFieldType.NUMBER).description("배달 ID"),
					fieldWithPath("deliveryStatus").type(JsonFieldType.STRING).description("배달 상태")
				)));
	}

	@DisplayName("배달기사를 배정할 수 있다.")
	@Test
	void allocateRider() throws Exception {
		// given
		DeliveryHistoryResponse response = DeliveryHistoryResponseBuilder.allocateRiderHistoryBuild();

		given(deliveryService.allocateRider(anyLong(), anyLong())).willReturn(response);

		// when
		ResultActions resultAction = mvc.perform(
			post("/api/deliveries/{deliveryId}/allocation/{riderId}", 1L, 1L)
				.accept(MediaType.APPLICATION_JSON)
		);

		// then
		resultAction.andExpect(status().isOk())
			.andDo(document("delivery-allocate",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("deliveryId").description("배달 ID"),
					parameterWithName("riderId").description("배달기사 ID")
				),
				responseFields(
					fieldWithPath("riderId").type(JsonFieldType.NUMBER).description("배달기사 ID"),
					fieldWithPath("historyInfo.deliveryStatus").type(JsonFieldType.STRING).description("배달 상태"),
					fieldWithPath("historyInfo.createdAt").type(JsonFieldType.STRING).description("생성 일시")
				)));
	}

	@DisplayName("배달을 시작할 수 있다.")
	@Test
	void startDelivery() throws Exception {
		// given
		DeliveryHistoryResponse response = DeliveryHistoryResponseBuilder.startDeliveryHistoryBuild();

		given(deliveryService.startDelivery(anyLong(), anyLong())).willReturn(response);

		// when
		ResultActions resultAction = mvc.perform(
			post("/api/deliveries/{deliveryId}/pick-up/{riderId}", 1L, 1L)
				.accept(MediaType.APPLICATION_JSON)
		);

		// then
		resultAction.andExpect(status().isOk())
			.andDo(document("delivery-start",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("deliveryId").description("배달 ID"),
					parameterWithName("riderId").description("배달기사 ID")
				),
				responseFields(
					fieldWithPath("riderId").type(JsonFieldType.NUMBER).description("배달기사 ID"),
					fieldWithPath("historyInfo.deliveryStatus").type(JsonFieldType.STRING).description("배달 상태"),
					fieldWithPath("historyInfo.createdAt").type(JsonFieldType.STRING).description("생성 일시")
				)));
	}

	@DisplayName("배달을 종료할 수 있다.")
	@Test
	void finishDelivery() throws Exception {
		// given
		DeliveryHistoryResponse response = DeliveryHistoryResponseBuilder.finishDeliveryHistoryBuild();

		given(deliveryService.finishDelivery(anyLong(), anyLong())).willReturn(response);

		// when
		ResultActions resultAction = mvc.perform(
			post("/api/deliveries/{deliveryId}/arrival/{riderId}", 1L, 1L)
				.accept(MediaType.APPLICATION_JSON)
		);

		// then
		resultAction.andExpect(status().isOk())
			.andDo(document("delivery-finish",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("deliveryId").description("배달 ID"),
					parameterWithName("riderId").description("배달기사 ID")
				),
				responseFields(
					fieldWithPath("riderId").type(JsonFieldType.NUMBER).description("배달기사 ID"),
					fieldWithPath("historyInfo.deliveryStatus").type(JsonFieldType.STRING).description("배달 상태"),
					fieldWithPath("historyInfo.createdAt").type(JsonFieldType.STRING).description("생성 일시")
				)));
	}

	@DisplayName("배달히스토리를 다건조회할 수 있다.")
	@Test
	void getDeliveryHistories() throws Exception {
		// given
		DeliveryHistoryResponse.Multiple response = DeliveryHistoryResponseBuilder.multipleDeliveryHistoryBuild();

		given(deliveryService.getDeliveryHistories(anyLong())).willReturn(response);

		// when
		ResultActions resultAction = mvc.perform(
			get("/api/deliveries/{deliveryId}", 1L)
				.accept(MediaType.APPLICATION_JSON)
		);

		// then
		resultAction.andExpect(status().isOk())
			.andDo(document("delivery-get-delivery-histories",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("deliveryId").description("배달 ID")
				),
				responseFields(
					fieldWithPath("riderId").type(JsonFieldType.NUMBER).description("배달기사 ID"),
					fieldWithPath("historyInfos[].deliveryStatus").type(JsonFieldType.STRING).description("배달 상태"),
					fieldWithPath("historyInfos[].createdAt").type(JsonFieldType.STRING).description("생성 일시"),
					fieldWithPath("historyInfos[].deliveryStatus").type(JsonFieldType.STRING).description("배달 상태"),
					fieldWithPath("historyInfos[].createdAt").type(JsonFieldType.STRING).description("생성 일시")
				)));
	}
}
