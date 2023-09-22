package com.prgrms.himin.order.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.himin.global.error.exception.BusinessException;
import com.prgrms.himin.global.error.exception.EntityNotFoundException;
import com.prgrms.himin.global.error.exception.ErrorCode;
import com.prgrms.himin.member.domain.Member;
import com.prgrms.himin.member.domain.MemberRepository;
import com.prgrms.himin.menu.domain.Menu;
import com.prgrms.himin.menu.domain.MenuOption;
import com.prgrms.himin.menu.domain.MenuOptionGroup;
import com.prgrms.himin.menu.domain.MenuOptionGroupRepository;
import com.prgrms.himin.menu.domain.MenuOptionRepository;
import com.prgrms.himin.menu.domain.MenuRepository;
import com.prgrms.himin.menu.domain.MenuValidator;
import com.prgrms.himin.order.domain.Order;
import com.prgrms.himin.order.domain.OrderHistory;
import com.prgrms.himin.order.domain.OrderHistoryRepository;
import com.prgrms.himin.order.domain.OrderItem;
import com.prgrms.himin.order.domain.OrderRepository;
import com.prgrms.himin.order.domain.SelectedOption;
import com.prgrms.himin.order.dto.request.OrderCreateRequest;
import com.prgrms.himin.order.dto.request.OrderSearchCondition;
import com.prgrms.himin.order.dto.request.SelectedMenuOptionRequest;
import com.prgrms.himin.order.dto.request.SelectedMenuRequest;
import com.prgrms.himin.order.dto.response.OrderResponse;
import com.prgrms.himin.order.dto.response.OrderResponses;
import com.prgrms.himin.shop.domain.Shop;
import com.prgrms.himin.shop.domain.ShopRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

	private final OrderRepository orderRepository;

	private final MenuRepository menuRepository;

	private final MemberRepository memberRepository;

	private final MenuOptionRepository menuOptionRepository;

	private final MenuOptionGroupRepository menuOptionGroupRepository;

	private final OrderHistoryRepository orderHistoryRepository;

	private final ShopRepository shopRepository;

	private final MenuValidator menuValidator;

	@Transactional
	public OrderResponse createOrder(OrderCreateRequest request) {
		Member member = memberRepository.findById(request.memberId())
			.orElseThrow(
				() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND)
			);

		Shop shop = shopRepository.findById(request.shopId())
			.orElseThrow(
				() -> new EntityNotFoundException(ErrorCode.SHOP_NOT_FOUND)
			);

		Order order = Order.builder()
			.address(request.address())
			.requirement(request.requirement())
			.shop(shop)
			.member(member)
			.build();

		List<SelectedMenuRequest> selectedMenus = request.selectedMenus();
		List<OrderItem> orderItems = extractOrderItems(
			selectedMenus,
			request.shopId()
		);
		attachOrderItems(order, orderItems);
		order.calculateOrderPrice();

		OrderHistory orderHistory = OrderHistory.createOrderHistory(order);

		Order savedOrder = orderRepository.save(order);
		orderHistoryRepository.save(orderHistory);

		return OrderResponse.from(savedOrder);
	}

	@Transactional
	public void finishOrder(Long orderId) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(
				() -> new EntityNotFoundException(ErrorCode.ORDER_NOT_FOUND)
			);

		OrderHistory orderHistory = OrderHistory.createDeliveredOrderHistory(order);
		orderHistoryRepository.save(orderHistory);
	}

	public OrderResponse getOrder(Long orderId) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(
				() -> new EntityNotFoundException(ErrorCode.ORDER_NOT_FOUND)
			);

		return OrderResponse.from(order);
	}

	@Transactional
	public void startCooking(
		Long shopId,
		Long orderId
	) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(
				() -> new EntityNotFoundException(ErrorCode.ORDER_NOT_FOUND)
			);

		validateShopId(
			shopId,
			order
		);

		OrderHistory orderHistory = OrderHistory.createStartedCookingOrderHistory(order);
		orderHistoryRepository.save(orderHistory);
	}

	@Transactional
	public void finishCooking(
		Long shopId,
		Long orderId
	) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(
				() -> new EntityNotFoundException(ErrorCode.ORDER_NOT_FOUND)
			);

		validateShopId(
			shopId,
			order
		);

		OrderHistory orderHistory = OrderHistory.createCookingCompletedOrderHistory(order);
		orderHistoryRepository.save(orderHistory);
	}

	private void attachOrderItems(
		Order order,
		List<OrderItem> orderItems
	) {
		for (OrderItem orderItem : orderItems) {
			orderItem.attachTo(order);
		}
	}

	private List<OrderItem> extractOrderItems(
		List<SelectedMenuRequest> selectedMenus,
		Long shopId
	) {
		List<OrderItem> orderItems = new ArrayList<>();
		for (SelectedMenuRequest selectedMenu : selectedMenus) {
			Menu menu = menuRepository.findById(selectedMenu.menuId())
				.orElseThrow(
					() -> new EntityNotFoundException(ErrorCode.MENU_NOT_FOUND)
				);

			menuValidator.validateShopId(
				shopId,
				menu
			);

			List<SelectedMenuOptionRequest> selectedMenuOptions = selectedMenu.selectedMenuOptions();

			List<SelectedOption> selectedOptions = extractSelectedOptions(
				selectedMenu.menuId(),
				selectedMenuOptions
			);

			int quantity = selectedMenu.quantity();
			OrderItem orderItem = new OrderItem(
				menu,
				quantity
			);

			selectedOptions.forEach(selectedOption -> selectedOption.attachTo(orderItem));
			orderItems.add(orderItem);
		}

		return orderItems;
	}

	private List<SelectedOption> extractSelectedOptions(
		Long menuId,
		List<SelectedMenuOptionRequest> selectedMenuOptions
	) {
		List<SelectedOption> selectedOptions = new ArrayList<>();

		for (SelectedMenuOptionRequest selectedMenuOption : selectedMenuOptions) {
			Long menuOptionGroupId = selectedMenuOption.menuOptionGroupId();
			MenuOptionGroup menuOptionGroup = menuOptionGroupRepository.findById(menuOptionGroupId)
				.orElseThrow(
					() -> new EntityNotFoundException(ErrorCode.MENU_OPTION_GROUP_NOT_FOUND)
				);

			menuValidator.validateMenuId(
				menuId,
				menuOptionGroup
			);

			List<Long> menuOptionIds = selectedMenuOption.selectedMenuOptions();

			List<MenuOption> menuOptions = extractMenuOptions(menuOptionGroupId, menuOptionIds);
			List<SelectedOption> toAdd = SelectedOption.from(menuOptions);
			selectedOptions.addAll(toAdd);
		}

		return selectedOptions;
	}

	private List<MenuOption> extractMenuOptions(
		Long menuOptionGroupId,
		List<Long> menuOptionIds
	) {
		List<MenuOption> menuOptions = new ArrayList<>();
		for (Long menuOptionId : menuOptionIds) {
			MenuOption menuOption = menuOptionRepository.findById(menuOptionId)
				.orElseThrow(
					() -> new EntityNotFoundException(ErrorCode.MENU_OPTION_NOT_FOUND)
				);

			menuValidator.validateMenuOptionGroupId(
				menuOptionGroupId,
				menuOption
			);

			menuOptions.add(menuOption);
		}

		return menuOptions;
	}

	private boolean isLast(List<Order> orders, int size) {
		return orders.size() <= size;
	}

	public OrderResponses getOrders(
		Long memberId,
		OrderSearchCondition orderSearchCondition,
		int size,
		Long cursor
	) {
		List<Order> orders = orderRepository.searchOrders(
			memberId,
			orderSearchCondition,
			size + 1,
			cursor
		);

		return new OrderResponses(
			getOrderResponses(orders),
			size,
			getNextCursor(orders),
			isLast(orders, size)
		);
	}

	private int getLastIndex(List<Order> orders) {
		return orders.size() - 1;
	}

	private Long getNextCursor(
		List<Order> orders
	) {
		if (orders.isEmpty()) {
			return null;
		}

		int lastIndex = getLastIndex(orders);
		return orders.get(lastIndex).getOrderId();
	}

	private List<OrderResponse> getOrderResponses(List<Order> orders) {
		return orders.stream()
			.map(OrderResponse::from)
			.toList();
	}

	private void validateShopId(
		Long shopId,
		Order order
	) {
		if (!order.getShop().getShopId().equals(shopId)) {
			throw new BusinessException(ErrorCode.ORDER_SHOP_NOT_MATCH);
		}
	}
}
