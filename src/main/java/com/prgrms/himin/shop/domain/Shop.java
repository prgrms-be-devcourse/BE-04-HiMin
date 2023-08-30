package com.prgrms.himin.shop.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.regex.Pattern;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "shops")
public class Shop {

    private static final int MAX_NAME_LENGTH = 20;

    private static final int MAX_ADDRESS_LENGTH = 50;

    private static final int MAX_PHONE_LENGTH = 15;

    private static final Pattern PHONE_PATTERN = Pattern.compile("^(02|0[3-9]{1}[0-9]{1}|010)-[0-9]{3,4}-[0-9]{4}$");

    private static final Pattern TIME_PATTERN = Pattern.compile("([01]?[0-9]|2[0-3]):[0-5][0-9]");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long shopId;

    @Column(name = "name", nullable = false, length = MAX_NAME_LENGTH)
    private String name;

    @Enumerated
    @Column(name = "category", nullable = false)
    private Category category;

    @Column(name = "address", nullable = false, length = MAX_ADDRESS_LENGTH)
    private String address;

    @Column(name = "phone", nullable = false, length = MAX_PHONE_LENGTH)
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
        validateName(name);
        validateAddress(address);
        validatePhone(phone);
        validateDeliveryTip(deliveryTip);
        validateOpeningTime(openingTime);
        validateClosingTime(closingTime);
        this.name = name;
        this.category = category;
        this.address = address;
        this.phone = phone;
        this.content = content;
        this.deliveryTip = deliveryTip;
        this.rating = BigDecimal.ZERO;
        this.status = ShopStatus.CLOSE;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    private void validateName(String name) {
        if (name == null || name.length() > MAX_NAME_LENGTH) {
            throw new RuntimeException("잘못된 이름입니다.");
        }
    }

    private void validateAddress(String address) {
        if (address == null || address.length() > MAX_ADDRESS_LENGTH) {
            throw new RuntimeException("잘못된 주소입니다.");
        }
    }

    private void validatePhone(String phone) {
        if (phone == null || !PHONE_PATTERN.matcher(phone).matches() || phone.length() > MAX_PHONE_LENGTH) {
            throw new RuntimeException("잘못된 전화번호입니다.");
        }
    }

    private void validateDeliveryTip(int deliveryTip) {
        if (deliveryTip < 0) {
            throw new RuntimeException("배달팁은 0원 이상이어야 합니다.");
        }
    }

    private void validateOpeningTime(String openingTime) {
        if (openingTime == null || !TIME_PATTERN.matcher(openingTime).matches()) {
            throw new RuntimeException("잘못된 오픈 시간입니다.");
        }
    }

    private void validateClosingTime(String closingTime) {
        if (openingTime == null || !TIME_PATTERN.matcher(closingTime).matches()) {
            throw new RuntimeException("잘못된 마감 시간입니다.");
        }
    }

    public void updateInfo(
            String name,
            Category category,
            String address,
            String phone,
            String content,
            int deliveryTip,
            String openingTime,
            String closingTime
    ) {
        validateName(name);
        validateAddress(address);
        validatePhone(phone);
        validateDeliveryTip(deliveryTip);
        validateOpeningTime(openingTime);
        validateClosingTime(closingTime);
        this.name = name;
        this.category = category;
        this.address = address;
        this.phone = phone;
        this.content = content;
        this.deliveryTip = deliveryTip;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }
}
