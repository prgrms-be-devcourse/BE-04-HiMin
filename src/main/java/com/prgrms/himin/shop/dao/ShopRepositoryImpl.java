package com.prgrms.himin.shop.dao;

import com.prgrms.himin.shop.domain.Category;
import com.prgrms.himin.shop.domain.Shop;
import com.prgrms.himin.shop.domain.ShopRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.prgrms.himin.shop.domain.QShop.shop;

@Repository
public class ShopRepositoryImpl implements ShopRepositoryCustom {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Shop> searchShops(String name,
                                  Category category,
                                  String address,
                                  Integer deliveryTip
    ) {
        return jpaQueryFactory.selectFrom(shop)
                .where(
                        containsName(name),
                        eqCategory(category),
                        containsAddress(address),
                        loeDeliveryTip(deliveryTip)
                )
                .fetch();
    }

    private BooleanExpression containsName(String name) {
        return name != null ? shop.name.contains(name) : null;
    }

    private BooleanExpression eqCategory(Category category) {
        return category != null ? shop.category.eq(category) : null;
    }

    private BooleanExpression containsAddress(String address) {
        return address != null ? shop.address.contains(address) : null;
    }

    private BooleanExpression loeDeliveryTip(Integer deliveryTip) {
        return deliveryTip != null ? shop.deliveryTip.loe(deliveryTip) : null;
    }
}
