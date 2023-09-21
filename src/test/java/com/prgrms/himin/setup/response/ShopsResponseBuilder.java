package com.prgrms.himin.setup.response;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.prgrms.himin.shop.domain.Category;
import com.prgrms.himin.shop.domain.ShopSort;
import com.prgrms.himin.shop.domain.ShopStatus;
import com.prgrms.himin.shop.dto.response.ShopResponse;
import com.prgrms.himin.shop.dto.response.ShopsResponse;

public class ShopsResponseBuilder {

	public static ShopsResponse successBuild() {
		List<ShopResponse> shopResponses = getShopResponses();

		return new ShopsResponse(
			shopResponses,
			3,
			4L,
			ShopSort.DELIVERY_TIP_ASC,
			true
		);
	}

	private static List<ShopResponse> getShopResponses() {
		List<ShopResponse> shopResponses = new ArrayList<>();
		for (int i = 2; i < 5; i++) {
			ShopResponse shopResponse = ShopResponse.builder()
				.shopId(Long.valueOf(i))
				.name("맥도날드 %d동점".formatted(i))
				.category(Category.FAST_FOOD)
				.address("경기도 광명시 광명%d동".formatted(i))
				.phone("02-261%d-222%d".formatted(i, i))
				.content("안녕하세요. 맥도날드 %d호점입니다.".formatted(i))
				.deliveryTip(1000 * i)
				.dibsCount(100)
				.status(ShopStatus.OPEN)
				.openingTime(LocalTime.of(9, 0))
				.closingTime(LocalTime.of(21, 0))
				.build();
			shopResponses.add(shopResponse);
		}

		return shopResponses;
	}
}
