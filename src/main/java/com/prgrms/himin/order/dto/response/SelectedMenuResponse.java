package com.prgrms.himin.order.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.prgrms.himin.order.domain.OrderItem;

import lombok.Builder;
import lombok.Getter;

@Getter
public final class SelectedMenuResponse {

	private final Long menuId;

	private final int quantity;

	private final List<Long> menuOptionIds;

	@Builder
	public SelectedMenuResponse(
		Long menuId,
		int quantity,
		List<Long> menuOptionNames
	) {
		this.menuId = menuId;
		this.quantity = quantity;
		this.menuOptionIds = menuOptionNames;
	}

	public static List<SelectedMenuResponse> from(List<OrderItem> orderItems) {
		List<SelectedMenuResponse> response = orderItems.stream()
			.map(SelectedMenuResponse::convertFrom)
			.collect(Collectors.toList());
		return response;
	}

	private static SelectedMenuResponse convertFrom(OrderItem orderItem) {
		SelectedMenuResponse response = SelectedMenuResponse.builder()
			.menuId(orderItem
				.getMenu()
				.getId()
			)
			.quantity(orderItem.getQuantity())
			.menuOptionNames(
				orderItem.getSelectedOptions()
					.stream()
					.map(selectedOption -> selectedOption
						.getMenuOption()
						.getId())
					.collect(Collectors.toList()))
			.build();
		return response;
	}
}
