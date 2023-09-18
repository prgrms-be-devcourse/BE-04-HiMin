package com.prgrms.himin.shop.dto.response;

import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.prgrms.himin.shop.domain.Category;
import com.prgrms.himin.shop.domain.Shop;
import com.prgrms.himin.shop.domain.ShopStatus;

import lombok.Builder;

@Builder
public record ShopResponse(
	Long shopId,
	String name,
	Category category,
	String address,
	String phone,
	String content,
	int deliveryTip,
	int dibsCount,
	ShopStatus status,

	@JsonFormat(pattern = "HH:mm")
	LocalTime openingTime,

	@JsonFormat(pattern = "HH:mm")
	LocalTime closingTime
) {

	public static ShopResponse from(Shop shop) {
		return ShopResponse.builder()
			.shopId(shop.getShopId())
			.name(shop.getName())
			.category(shop.getCategory())
			.address(shop.getAddress())
			.phone(shop.getPhone())
			.content(shop.getContent())
			.deliveryTip(shop.getDeliveryTip())
			.dibsCount(shop.getDibsCount())
			.status(shop.getStatus())
			.openingTime(shop.getOpeningTime())
			.closingTime(shop.getClosingTime())
			.build();
	}

	public static List<ShopResponse> from(List<Shop> shops) {
		return shops.stream()
			.map(ShopResponse::from)
			.toList();
	}
}
