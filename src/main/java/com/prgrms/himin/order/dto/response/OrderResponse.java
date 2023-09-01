package com.prgrms.himin.order.dto.response;

import java.util.List;

import com.prgrms.himin.order.domain.Order;

import lombok.Builder;
import lombok.Getter;

@Getter
public final class OrderResponse {

	private final Long orderId;

	private final Long memberId;

	private final Long shopId;

	private final String address;

	private final String requirement;

	private final List<SelectedMenuResponse> selectedMenus;

	private final int price;

	@Builder
	public OrderResponse(
		Long orderId,
		Long memberId,
		Long shopId,
		String address,
		String requirement,
		List<SelectedMenuResponse> selectedMenus,
		int price) {
		this.orderId = orderId;
		this.memberId = memberId;
		this.shopId = shopId;
		this.address = address;
		this.requirement = requirement;
		this.selectedMenus = selectedMenus;
		this.price = price;
	}

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
