package com.prgrms.himin.shop.dto.request;

import com.prgrms.himin.shop.domain.Category;
import com.prgrms.himin.shop.domain.Shop;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public final class ShopCreateRequest {

    private final String name;

    private final Category category;

    private final String address;

    private final String phone;

    private final String content;

    private final int deliveryTip;

    private final String openingTime;

    private final String closingTime;

    public Shop toEntity() {
        return Shop.builder()
                .name(this.name)
                .category(this.category)
                .address(this.address)
                .phone(this.phone)
                .content(this.content)
                .deliveryTip(this.deliveryTip)
                .openingTime(this.openingTime)
                .closingTime(this.closingTime)
                .build();
    }
}
