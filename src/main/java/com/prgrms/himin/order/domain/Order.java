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

import com.prgrms.himin.global.error.exception.BusinessException;
import com.prgrms.himin.global.error.exception.ErrorCode;
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

	public static final int MAX_ADDRESS_LENGTH = 50;

	public static final int MAX_REQUIREMENT_LENGTH = 30;

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
		validateShop(shop);
		validateMember(member);
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

	public void calculateOrderPrice() {
		for (OrderItem orderItem : orderItems) {
			price += orderItem.calculateOrderItemPrice();
		}
		price += shop.getDeliveryTip();
	}

	private void validateAddress(String address) {
		if (address == null || address.length() > MAX_ADDRESS_LENGTH) {
			throw new BusinessException(ErrorCode.MEMBER_ADDRESS_BAD_REQUEST);
		}
	}

	private void validateRequirement(String requirement) {
		if (requirement == null || requirement.length() > MAX_REQUIREMENT_LENGTH) {
			throw new BusinessException(ErrorCode.ORDER_REQUIREMENT_BAD_REQUEST);
		}
	}

	private void validateMember(Member member) {
		if (member == null) {
			throw new BusinessException(ErrorCode.MEMBER_BAD_REQUEST);
		}
	}

	private void validateShop(Shop shop) {
		if (shop == null) {
			throw new BusinessException(ErrorCode.SHOP_BAD_REQUEST);
		}
	}
}
