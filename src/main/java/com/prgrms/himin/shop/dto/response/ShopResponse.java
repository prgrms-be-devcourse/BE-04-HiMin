package com.prgrms.himin.shop.dto.response;

import com.prgrms.himin.shop.domain.Category;
import com.prgrms.himin.shop.domain.Shop;
import com.prgrms.himin.shop.domain.ShopStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public final class ShopResponse {

    private final Long shopId;

    private final String name;

    private final Category category;

    private final String address;

    private final String phone;

    private final String content;

    private final int deliveryTip;

    private final BigDecimal rating;

    private final int dibsCount;

    private final ShopStatus status;

    private final String openingTime;

    private final String closingTime;

    @Builder
    private ShopResponse(
            Long shopId,
            String name,
            Category category,
            String address,
            String phone,
            String content,
            int deliveryTip,
            BigDecimal rating,
            int dibsCount,
            ShopStatus status,
            String openingTime,
            String closingTime
    ) {
        this.shopId = shopId;
        this.name = name;
        this.category = category;
        this.address = address;
        this.phone = phone;
        this.content = content;
        this.deliveryTip = deliveryTip;
        this.rating = rating;
        this.dibsCount = dibsCount;
        this.status = status;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public static ShopResponse from(Shop shop) {
        return ShopResponse.builder()
                .shopId(shop.getShopId())
                .name(shop.getName())
                .category(shop.getCategory())
                .address(shop.getAddress())
                .phone(shop.getPhone())
                .content(shop.getContent())
                .deliveryTip(shop.getDeliveryTip())
                .rating(shop.getRating())
                .dibsCount(shop.getDibsCount())
                .status(shop.getStatus())
                .openingTime(shop.getOpeningTime())
                .closingTime(shop.getClosingTime())
                .build();
    }
}
