package com.prgrms.himin.shop.domain;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.prgrms.himin.global.error.exception.ErrorCode;
import com.prgrms.himin.global.error.exception.InvalidValueException;
import com.prgrms.himin.global.util.PhonePolicy;
import com.prgrms.himin.menu.domain.Menu;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "shops")
public class Shop {

	public static final int MAX_NAME_LENGTH = 20;

	public static final int MAX_ADDRESS_LENGTH = 50;

	public static final int MAX_PHONE_LENGTH = 15;

	public static final String TIME_FORMAT = "HH:mm";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long shopId;

	@Column(name = "name", nullable = false, length = MAX_NAME_LENGTH)
	private String name;

	@Enumerated(EnumType.STRING)
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

	@Column(name = "dibs_count")
	private int dibsCount;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private ShopStatus status;

	@Column(name = "opening_time", nullable = false)
	private LocalTime openingTime;

	@Column(name = "closing_time", nullable = false)
	private LocalTime closingTime;

	@OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Menu> menus = new ArrayList<>();

	@Builder
	private Shop(
		String name,
		Category category,
		String address,
		String phone,
		String content,
		int deliveryTip,
		LocalTime openingTime,
		LocalTime closingTime
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
		this.status = ShopStatus.CLOSE;
		this.openingTime = openingTime;
		this.closingTime = closingTime;
	}

	public void addMenu(Menu menu) {
		if (!menus.contains(menu)) {
			this.menus.add(menu);
		}
	}

	public void removeMenu(Menu menu) {
		this.menus.remove(menu);
	}

	private void validateName(String name) {
		if (name == null || name.isBlank() || name.length() > MAX_NAME_LENGTH) {
			throw new InvalidValueException(ErrorCode.SHOP_NAME_BAD_REQUEST);
		}
	}

	private void validateAddress(String address) {
		if (address == null || address.length() > MAX_ADDRESS_LENGTH) {
			throw new InvalidValueException(ErrorCode.SHOP_ADDRESS_BAD_REQUEST);
		}
	}

	private void validatePhone(String phone) {
		if (phone == null || !PhonePolicy.matches(phone) || phone.length() > MAX_PHONE_LENGTH) {
			throw new InvalidValueException(ErrorCode.SHOP_PHONE_BAD_REQUEST);
		}
	}

	private void validateDeliveryTip(int deliveryTip) {
		if (deliveryTip < 0) {
			throw new InvalidValueException(ErrorCode.SHOP_DELIVERY_TIP_BAD_REQUEST);
		}
	}

	private void validateOpeningTime(LocalTime openingTime) {
		if (openingTime == null) {
			throw new InvalidValueException(ErrorCode.SHOP_OPENING_TIME_BAD_REQUEST);
		}
	}

	private void validateClosingTime(LocalTime closingTime) {
		if (closingTime == null) {
			throw new InvalidValueException(ErrorCode.SHOP_CLOSING_TIME_BAD_REQUEST);
		}
	}

	public void updateInfo(
		String name,
		Category category,
		String address,
		String phone,
		String content,
		int deliveryTip,
		LocalTime openingTime,
		LocalTime closingTime
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

	public void changeStatus(ShopStatus status) {
		this.status = status;
	}
}
