package com.prgrms.himin.shop.domain;

import static com.prgrms.himin.shop.domain.QShop.*;

import java.util.HashMap;
import java.util.Map;

import com.prgrms.himin.global.error.exception.ErrorCode;
import com.prgrms.himin.global.error.exception.InvalidValueException;
import com.querydsl.core.types.OrderSpecifier;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShopSort {

	DELIVERY_TIP_ASC("deliveryTipAsc", shop.deliveryTip.asc());

	private final String name;
	private final OrderSpecifier<Integer> orderSpecifier;
	private static final Map<String, ShopSort> SHOP_SORT_MAP = new HashMap<>();

	static {
		for (ShopSort sort : values()) {
			SHOP_SORT_MAP.put(sort.name, sort);
		}
	}

	public static ShopSort from(String name) {
		if (name == null) {
			return null;
		}

		if (SHOP_SORT_MAP.containsKey(name)) {
			return SHOP_SORT_MAP.get(name);
		}

		throw new InvalidValueException(ErrorCode.SHOP_SORT_BAD_REQUEST);
	}
}
