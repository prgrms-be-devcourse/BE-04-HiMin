package com.prgrms.himin.setup.response;

import java.time.LocalTime;

import com.prgrms.himin.shop.domain.Category;
import com.prgrms.himin.shop.domain.ShopStatus;
import com.prgrms.himin.shop.dto.response.ShopResponse;

public class ShopResponseBuilder {

	public static ShopResponse successBuild() {
		return new ShopResponse(
			1L,
			"맥도날드",
			Category.FAST_FOOD,
			"경기도 광명시 광명동",
			"02-2611-2222",
			"안녕하세요. 맥도날드입니다.",
			1000,
			0,
			ShopStatus.CLOSE,
			LocalTime.of(9, 0),
			LocalTime.of(21, 0)
		);
	}
}
