package com.prgrms.himin.setup.response;

import java.util.List;

import com.prgrms.himin.order.dto.response.OrderResponse;
import com.prgrms.himin.order.dto.response.SelectedMenuResponse;

public class OrderResponseBuilder {

	public static OrderResponse successBuild() {
		List<SelectedMenuResponse> selectedMenuResponses = getSelectedMenuResponses();

		return new OrderResponse(
			1L,
			1L,
			1L,
			"성동구 사근동",
			"문앞에 두고 문자부탁드려요",
			selectedMenuResponses,
			20000
		);
	}

	public static OrderResponse successBuild(
		Long orderId,
		Long memberId,
		Long shopId,
		Long menuId,
		int quantity,
		List<Long> selectedMenuOptions
	) {
		List<SelectedMenuResponse> selectedMenuResponses = getSelectedMenuResponses(
			menuId,
			quantity,
			selectedMenuOptions
		);

		return new OrderResponse(
			orderId,
			memberId,
			shopId,
			"성동구 사근동",
			"문앞에 두고 문자부탁드려요",
			selectedMenuResponses,
			20000
		);
	}

	private static List<SelectedMenuResponse> getSelectedMenuResponses(
		Long menuId,
		int quantity,
		List<Long> selectedMenuOptions
	) {
		SelectedMenuResponse selectedMenuResponse = new SelectedMenuResponse(
			menuId,
			quantity,
			selectedMenuOptions
		);

		return List.of(selectedMenuResponse);
	}

	private static List<SelectedMenuResponse> getSelectedMenuResponses() {
		SelectedMenuResponse selectedMenuResponse = new SelectedMenuResponse(1L, 5, List.of(1L, 2L, 3L));
		return List.of(selectedMenuResponse);
	}
}
