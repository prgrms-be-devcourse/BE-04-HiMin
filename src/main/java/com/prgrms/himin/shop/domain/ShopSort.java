package com.prgrms.himin.shop.domain;

import static com.prgrms.himin.shop.domain.QShop.*;

import com.querydsl.core.types.OrderSpecifier;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShopSort {

	DELIVERY_TIP_ASC("deliveryTip", shop.deliveryTip.asc());

	private final String name;
	private final OrderSpecifier<Integer> orderSpecifier;
}
