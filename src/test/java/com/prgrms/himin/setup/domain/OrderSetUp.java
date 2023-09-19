package com.prgrms.himin.setup.domain;

import java.util.List;

import org.springframework.stereotype.Component;

import com.prgrms.himin.member.domain.Member;
import com.prgrms.himin.order.domain.Order;
import com.prgrms.himin.order.domain.OrderItem;
import com.prgrms.himin.order.domain.OrderRepository;
import com.prgrms.himin.order.domain.SelectedOption;
import com.prgrms.himin.shop.domain.Shop;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class OrderSetUp {

	private final OrderRepository orderRepository;

	public Order saveOne(
		Member member,
		Shop shop,
		OrderItem orderItem,
		List<SelectedOption> selectedOptions
	) {
		Order order = Order.builder()
			.member(member)
			.shop(shop)
			.requirement("문앞에 두고 가주세요")
			.address("서울시 사근동")
			.build();

		selectedOptions.forEach(selectedOption -> selectedOption.attachTo(orderItem));
		orderItem.attachTo(order);

		order.calculateOrderPrice();
		orderRepository.save(order);

		return order;
	}
}
