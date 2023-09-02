package com.prgrms.himin.order.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.prgrms.himin.order.domain.OrderItem;

public record SelectedMenuResponse(
	Long menuId,
	int quantity,
	List<Long> selectedOptionIds
) {

	public static List<SelectedMenuResponse> from(List<OrderItem> orderItems) {
		List<SelectedMenuResponse> response = orderItems.stream()
			.map(SelectedMenuResponse::convertFrom)
			.collect(Collectors.toList());

		return response;
	}

	private static SelectedMenuResponse convertFrom(OrderItem orderItem) {
		SelectedMenuResponse response = new SelectedMenuResponse(
			orderItem.getMenu().getId(),
			orderItem.getQuantity(),
			orderItem.getSelectedOptions()
				.stream()
				.map(selectedOption -> selectedOption.getMenuOption().getId())
				.collect(Collectors.toList())
		);

		return response;
	}
}
