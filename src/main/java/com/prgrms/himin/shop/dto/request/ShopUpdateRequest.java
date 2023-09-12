package com.prgrms.himin.shop.dto.request;

import static com.prgrms.himin.shop.domain.Shop.*;

import java.time.LocalTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.prgrms.himin.global.common.ValidEnum;
import com.prgrms.himin.global.util.PhonePolicy;
import com.prgrms.himin.shop.domain.Category;
import com.prgrms.himin.shop.domain.ShopStatus;

public record ShopUpdateRequest() {

	public record Info(
		@Size(max = MAX_NAME_LENGTH, message = "가게 이름은 최대 {max}글자 입니다.")
		@NotBlank(message = "이름이 비어있으면 안됩니다.")
		String name,

		@ValidEnum(enumClass = Category.class)
		String category,

		@Size(max = MAX_ADDRESS_LENGTH, message = "주소는 최대 {max}글자 입니다.")
		@NotBlank(message = "주소가 비어있으면 안됩니다.")
		String address,

		@Size(max = MAX_PHONE_LENGTH, message = "전화번호는 최대 {max}글자 입니다.")
		@NotBlank(message = "전화번호가 비어있으면 안됩니다.")
		@Pattern(regexp = PhonePolicy.PHONE_PATTERN, message = "전화번호 형식이어야 합니다.")
		String phone,

		String content,

		@PositiveOrZero(message = "배달팁은 0원 이상이어야 합니다.")
		int deliveryTip,

		@NotNull(message = "오픈 시간이 비어있으면 안됩니다.")
		@DateTimeFormat(pattern = "HH:mm")
		LocalTime openingTime,

		@NotNull(message = "마감 시간이 비어있으면 안됩니다.")
		@DateTimeFormat(pattern = "HH:mm")
		LocalTime closingTime
	) {
	}

	public record Status(
		@ValidEnum(enumClass = ShopStatus.class)
		String status
	) {
	}
}
