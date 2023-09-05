package com.prgrms.himin.setup.request;

import java.time.LocalTime;

import com.prgrms.himin.shop.domain.Category;
import com.prgrms.himin.shop.domain.ShopStatus;
import com.prgrms.himin.shop.dto.request.ShopUpdateRequest;

public class ShopUpdateRequestBuilder {

	public static ShopUpdateRequest.Status statusSuccessBuild() {
		return new ShopUpdateRequest.Status(ShopStatus.OPEN.toString());
	}

	public static ShopUpdateRequest.Status statusFailBuild(String status) {
		return new ShopUpdateRequest.Status(status);
	}

	public static ShopUpdateRequest.Info infoSuccessBuild() {
		return new ShopUpdateRequest.Info(
			"롯데리아",
			Category.FAST_FOOD.toString(),
			"경기도 광명시 광명동",
			"02-2633-4444",
			"안녕하세요. 롯데리아입니다.",
			5000,
			LocalTime.of(8, 0),
			LocalTime.of(22, 0)
		);
	}

	public static ShopUpdateRequest.Info infoFailBuild(String name) {
		return new ShopUpdateRequest.Info(
			name,
			Category.FAST_FOOD.toString(),
			"경기도 광명시 광명동",
			"02-2633-4444",
			"안녕하세요. 롯데리아입니다.",
			5000,
			LocalTime.of(8, 0),
			LocalTime.of(22, 0)
		);
	}
}
