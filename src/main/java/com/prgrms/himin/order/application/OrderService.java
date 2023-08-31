package com.prgrms.himin.order.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.himin.global.error.exception.EntityNotFoundException;
import com.prgrms.himin.global.error.exception.ErrorCode;
import com.prgrms.himin.member.domain.Member;
import com.prgrms.himin.member.domain.MemberRepository;
import com.prgrms.himin.menu.domain.Menu;
import com.prgrms.himin.menu.domain.MenuOption;
import com.prgrms.himin.menu.domain.MenuOptionRepository;
import com.prgrms.himin.menu.domain.MenuRepository;
import com.prgrms.himin.order.domain.Order;
import com.prgrms.himin.order.domain.OrderHistory;
import com.prgrms.himin.order.domain.OrderHistoryRepository;
import com.prgrms.himin.order.domain.OrderItem;
import com.prgrms.himin.order.domain.OrderRepository;
import com.prgrms.himin.order.domain.OrderStatus;
import com.prgrms.himin.order.domain.SelectedOption;
import com.prgrms.himin.order.dto.request.OrderCreateRequest;
import com.prgrms.himin.order.dto.request.SelectedMenuRequest;
import com.prgrms.himin.order.dto.response.OrderResponse;
import com.prgrms.himin.shop.domain.Shop;
import com.prgrms.himin.shop.domain.ShopRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

	private final OrderRepository orderRepository;

	private final MenuRepository menuRepository;

	private final MemberRepository memberRepository;

	private final MenuOptionRepository menuOptionRepository;

	private final OrderHistoryRepository orderHistoryRepository;

	private final ShopRepository shopRepository;

	@Transactional
	public OrderResponse createOrder(OrderCreateRequest request) {
		Member member = memberRepository.findById(request.getMemberId())
			.orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

		Shop shop = shopRepository.findById(request.getShopId())
			.orElseThrow(() -> new RuntimeException("찾는 shop이 존재하지 않습니다."));

		Order order = Order.builder()
			.address(request.getAddress())
			.requirement(request.getRequirement())
			.shop(shop)
			.member(member)
			.build();

		List<SelectedMenuRequest> selectedMenus = request.getSelectedMenus();
		List<OrderItem> orderItems = extractOrderItems(selectedMenus);
		log.info("orderItems size : {}", orderItems.size());
		log.info("orderItems SelectedOption size : {}", orderItems.get(0).getSelectedOptions().size());
		attachOrderItem(order, orderItems);
		OrderHistory orderHistory = new OrderHistory(
			order,
			OrderStatus.ORDERED
		);

		orderRepository.save(order);
		orderHistoryRepository.save(orderHistory);

		return OrderResponse.from(order);
	}

	private void attachOrderItem(Order order, List<OrderItem> orderItems) {
		for (OrderItem orderItem : orderItems) {
			orderItem.attachTo(order);
			order.addOrderPrice(orderItem.calculateOrderItemPrice());
		}
	}

	private List<OrderItem> extractOrderItems(List<SelectedMenuRequest> selectedMenus) {
		List<OrderItem> orderItems = new ArrayList<>();
		for (SelectedMenuRequest selectedMenu : selectedMenus) {
			Menu menu = menuRepository.findById(selectedMenu.getMenuId())
				.orElseThrow(() -> new RuntimeException("찾는 menu가 존재하지 않습니다."));

			List<Long> menuOptionIds = selectedMenu.getMenuOptionIdList();
			List<MenuOption> menuOptions = extractMenuOptions(menuOptionIds);

			log.info("before menuoptions size : {}", menuOptions.size());
			int quantity = selectedMenu.getQuantity();
			OrderItem orderItem = new OrderItem(
				menu,
				quantity
			);

			menuOptions.stream()
				.map(SelectedOption::new)
				.forEach(selectedOption -> selectedOption.attachTo(orderItem));
			orderItems.add(orderItem);
		}
		return orderItems;
	}

	private List<MenuOption> extractMenuOptions(List<Long> menuOptionIds) {
		List<MenuOption> menuOptions = new ArrayList<>();
		for (Long menuOptionId : menuOptionIds) {
			MenuOption menuOption = menuOptionRepository.findById(menuOptionId)
				.orElseThrow(() -> new RuntimeException("찾는 menuOption이 존재하지 않습니다."));
			menuOptions.add(menuOption);
		}
		log.info("menuOptions Size : {}", menuOptions.size());
		log.info("menuOptionIds Size : {}", menuOptionIds.size());
		return menuOptions;
	}
}
