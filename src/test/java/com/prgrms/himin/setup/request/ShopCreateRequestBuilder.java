package com.prgrms.himin.setup.request;

import java.time.LocalTime;

import com.prgrms.himin.shop.domain.Category;
import com.prgrms.himin.shop.dto.request.ShopCreateRequest;

public class ShopCreateRequestBuilder {

	public static ShopCreateRequest successBuild() {
		return new ShopCreateRequest(
			"맥도날드",
			Category.FAST_FOOD.toString(),
			"경기도 광명시 광명동",
			"02-2611-2222",
			"안녕하세요. 맥도날드입니다.",
			1000,
			LocalTime.of(9, 0),
			LocalTime.of(21, 0)
		);
	}

	public static ShopCreateRequest failBuild(
		String name,
		Category category
	) {
		return new ShopCreateRequest(
			name,
			category.toString(),
			"경기도 광명시 광명동",
			"02-2611-2222",
			"안녕하세요. %s입니다.".formatted(name),
			1000,
			LocalTime.of(9, 0),
			LocalTime.of(21, 0)
		);
	}
}
