package com.prgrms.himin.shop.dto.request;

import com.prgrms.himin.shop.domain.Category;
import com.prgrms.himin.shop.domain.ShopStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

public class ShopUpdateRequest {

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PROTECTED)
    public static final class Info {

        @Size(max = 20, message = "가게 이름은 최대 20글자 입니다.")
        @NotBlank(message = "이름이 비어있으면 안됩니다.")
        private final String name;

        private final Category category;

        @Size(max = 50, message = "주소는 최대 50글자 입니다.")
        @NotBlank(message = "주소가 비어있으면 안됩니다.")
        private final String address;

        @Size(max = 15, message = "전화번호는 최대 15글자 입니다.")
        @NotBlank(message = "전화번호가 비어있으면 안됩니다.")
        @Pattern(regexp = "^(02|0[3-9]{1}[0-9]{1}|010)-[0-9]{3,4}-[0-9]{4}$", message = "전화번호 형식이어야 합니다.")
        private final String phone;

        private final String content;

        @PositiveOrZero(message = "배달팁은 0원 이상이어야 합니다.")
        private final int deliveryTip;

        @NotBlank(message = "오픈 시간이 비어있으면 안됩니다.")
        @Pattern(regexp = "([01]?[0-9]|2[0-3]):[0-5][0-9]", message = "오픈 시간은 HH:MM 형식이어야 합니다.")
        private final String openingTime;

        @NotBlank(message = "마감 시간이 비어있으면 안됩니다.")
        @Pattern(regexp = "([01]?[0-9]|2[0-3]):[0-5][0-9]", message = "마감 시간은 HH:MM 형식이어야 합니다.")
        private final String closingTime;
    }

    @Getter
    public static final class Status {

        private ShopStatus status;
    }
}
