package com.prgrms.himin.shop.dto.response;

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
    String openingTime,
    String closingTime
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
}
