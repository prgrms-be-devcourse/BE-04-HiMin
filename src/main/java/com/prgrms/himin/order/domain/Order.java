package com.prgrms.himin.order.domain;

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
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long orderId;

	@Column(name = "address", nullable = false, length = 50)
	private String address;

	@Enumerated
	@Column(name = "status", nullable = false)
	private OrderStatus status;

	@Column(name = "requirement", length = 30)
	private String requirement;

	@Column(name = "price", nullable = false)
	private int price;

	@Column(name = "order_time", nullable = false)
	private LocalTime orderTime;

	@Column(name = "arrival_time")
	private LocalTime arrivalTime;
}
