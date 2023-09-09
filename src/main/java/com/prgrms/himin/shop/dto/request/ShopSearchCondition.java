package com.prgrms.himin.shop.dto.request;

import com.prgrms.himin.shop.domain.Category;

public record ShopSearchCondition(

	String name,

	Category category,

	String address,

	Integer deliveryTip,

	String menuName
) {
}

