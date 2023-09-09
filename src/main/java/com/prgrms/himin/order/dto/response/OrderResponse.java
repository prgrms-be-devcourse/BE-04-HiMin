package com.prgrms.himin.order.dto.response;

import java.util.List;

import com.prgrms.himin.order.domain.Order;

import lombok.Builder;

@Builder
public record OrderResponse(
	Long orderId,
	Long memberId,
	Long shopId,
	String address,
	String requirement,
	List<SelectedMenuResponse> selectedMenus,
	int price
) {

	public static OrderResponse from(Order order) {
		List<SelectedMenuResponse> selectedMenus = SelectedMenuResponse.from(order.getOrderItems());

		return OrderResponse.builder()
			.orderId(order.getOrderId())
			.memberId(order.getMember().getId())
			.shopId(order.getShop().getShopId())
			.address(order.getAddress())
			.requirement(order.getRequirement())
			.selectedMenus(selectedMenus)
			.price(order.getPrice())
			.build();
	}
}
