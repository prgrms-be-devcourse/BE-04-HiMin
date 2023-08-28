package com.prgrms.himin.shop.domain;

import java.math.BigDecimal;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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
	private LocalTime openingTime;

	@Column(name = "closing_time", nullable = false)
	private LocalTime closingTime;
}
