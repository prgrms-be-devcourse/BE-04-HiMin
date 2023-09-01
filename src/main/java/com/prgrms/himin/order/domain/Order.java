package com.prgrms.himin.order.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.prgrms.himin.member.domain.Member;
import com.prgrms.himin.shop.domain.Shop;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "orders")
public class Order {

	private static final int MIN_ORDER_PRICE = 0;

	private static final int MAX_ADDRESS_LENGTH = 50;

	private static final int MAX_REQUIREMENT_LENGTH = 30;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long orderId;

	@Column(name = "address", nullable = false, length = MAX_ADDRESS_LENGTH)
	private String address;

	@Column(name = "requirement", length = MAX_REQUIREMENT_LENGTH)
	private String requirement;

	@Column(name = "price", nullable = false)
	private int price;

	@Column(name = "order_time", nullable = false)
	private LocalDateTime orderTime;

	@Column(name = "arrival_time")
	private LocalDateTime arrivalTime;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderItem> orderItems = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shop_id", nullable = false)
	private Shop shop;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Builder
	public Order(
		String address,
		String requirement,
		Shop shop,
		Member member
	) {
		validateAddress(address);
		validateRequirement(requirement);
		this.address = address;
		this.requirement = requirement;
		this.shop = shop;
		this.member = member;
		this.orderTime = LocalDateTime.now();
	}

	public void addOrderItem(OrderItem orderItem) {
		if (orderItems.contains(orderItem)) {
			return;
		}
		orderItems.add(orderItem);
	}

	public void removeOrderItem(OrderItem orderItem) {
		orderItems.remove(orderItem);
	}

	public void addOrderPrice(int orderItemPrice) {
		if (orderItemPrice < MIN_ORDER_PRICE) {
			throw new RuntimeException("가격이 0이하일 수 없습니다.");
		}
		price += orderItemPrice;
	}

	private void validateAddress(String address) {
		if (address == null || address.length() > MAX_ADDRESS_LENGTH) {
			throw new RuntimeException("잘못된 주소입니다.");
		}
	}

	private void validateRequirement(String requirement) {
		if (requirement == null || requirement.length() > MAX_REQUIREMENT_LENGTH) {
			throw new RuntimeException("잘못된 요청사항입니다.");
		}
	}
}
