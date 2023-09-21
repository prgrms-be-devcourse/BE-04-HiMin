package com.prgrms.himin.setup.response;

import java.util.List;

import com.prgrms.himin.order.dto.response.OrderResponse;
import com.prgrms.himin.order.dto.response.OrderResponses;

public class OrderResponsesBuilder {

	public static OrderResponses successBuild() {
		OrderResponse orderResponse1 = OrderResponseBuilder.successBuild(
			1L,
			1L,
			1L,
			1L,
			10,
			List.of(1L, 2L, 3L)
		);

		OrderResponse orderResponse2 = OrderResponseBuilder.successBuild(
			2L,
			1L,
			1L,
			2L,
			10,
			List.of(4L, 5L)
		);

		List<OrderResponse> orderResponses = List.of(orderResponse1, orderResponse2);

		return new OrderResponses(
			orderResponses,
			10,
			null,
			true
		);
	}
}
