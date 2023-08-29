package com.prgrms.himin.shop.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "shops")
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long shopId;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Enumerated
    @Column(name = "category", nullable = false)
    private Category category;

    @Column(name = "address", nullable = false, length = 50)
    private String address;

    @Column(name = "phone", nullable = false, length = 15)
    private String phone;

    @Column(name = "content")
    private String content;

    @Column(name = "delivery_tip", nullable = false)
    private int deliveryTip;

    @Column(name = "rating")
    private BigDecimal rating;

    @Column(name = "dibs_count")
    private int dibsCount;

    @Enumerated
    @Column(name = "status", nullable = false)
    private ShopStatus status;

    @Column(name = "opening_time", nullable = false)
    private String openingTime;

    @Column(name = "closing_time", nullable = false)
    private String closingTime;

    @Builder
    private Shop(
            String name,
            Category category,
            String address,
            String phone,
            String content,
            int deliveryTip,
            String openingTime,
            String closingTime
    ) {
        this.name = name;
        this.category = category;
        this.address = address;
        this.phone = phone;
        this.content = content;
        this.deliveryTip = deliveryTip;
        this.status = ShopStatus.CLOSE;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }
}
