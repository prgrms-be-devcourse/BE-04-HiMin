package com.prgrms.himin.shop.domain;

import java.util.List;

public interface ShopRepositoryCustom {

    List<Shop> searchShops(
            String name,
            Category category,
            String address,
            Integer deliveryTip
    );
}
