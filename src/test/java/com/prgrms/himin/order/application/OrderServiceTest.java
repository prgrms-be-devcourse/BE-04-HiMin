package com.prgrms.himin.order.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.AdditionalAnswers.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.prgrms.himin.member.domain.Member;
import com.prgrms.himin.member.domain.MemberRepository;
import com.prgrms.himin.menu.domain.MenuValidator;
import com.prgrms.himin.order.domain.Order;
import com.prgrms.himin.order.domain.OrderHistoryRepository;
import com.prgrms.himin.order.domain.OrderRepository;
import com.prgrms.himin.order.dto.request.OrderCreateRequest;
import com.prgrms.himin.order.dto.request.SelectedMenuRequest;
import com.prgrms.himin.order.dto.response.OrderResponse;
import com.prgrms.himin.setup.factory.OrderDomainTestDependencyFactory;
import com.prgrms.himin.setup.factory.SelectedMenuFactory;
import com.prgrms.himin.setup.request.OrderCreateRequestBuilder;
import com.prgrms.himin.shop.domain.Shop;
import com.prgrms.himin.shop.domain.ShopRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	@Mock
	OrderRepository orderRepository;

	@Mock
	MemberRepository memberRepository;

	@Mock
	ShopRepository shopRepository;

	@Mock
	MenuValidator menuValidator;

	@Mock
	OrderHistoryRepository orderHistoryRepository;

	@InjectMocks
	OrderService orderService;

	OrderDomainTestDependencyFactory orderDomainTestDependencyFactory;

	SelectedMenuFactory selectedMenuFactory;

	@BeforeEach
	void each() {
		orderDomainTestDependencyFactory = new OrderDomainTestDependencyFactory();
		orderDomainTestDependencyFactory.initByOrder();
		selectedMenuFactory = new SelectedMenuFactory(orderDomainTestDependencyFactory);
		selectedMenuFactory.initSelectedMenuFactory();
	}

	@Nested
	@DisplayName("주문 생성을 할 수 있다.")
	class CreateOrder {

		void initMockRepository() {
			Member member = orderDomainTestDependencyFactory.getMember();
			Shop shop = orderDomainTestDependencyFactory.getShop();

			given(memberRepository.findById(member.getId()))
				.willReturn(Optional.of(member));

			given(shopRepository.findById(shop.getShopId()))
				.willReturn(Optional.of(shop));

			when(orderRepository.save(any(Order.class)))
				.then(returnsFirstArg());
		}

		OrderResponse getExpectedOrderResponse(OrderCreateRequest orderCreateRequest) {
			Member member = orderDomainTestDependencyFactory.getMember();
			Shop shop = orderDomainTestDependencyFactory.getShop();

			Order order = Order.builder()
				.address(orderCreateRequest.address())
				.requirement(orderCreateRequest.requirement())
				.member(member)
				.shop(shop)
				.build();

			return OrderResponse.from(order);
		}

		@DisplayName("성공한다.")
		@Test
		void success_test() {
			// given
			Member member = orderDomainTestDependencyFactory.getMember();
			Shop shop = orderDomainTestDependencyFactory.getShop();

			initMockRepository();

			List<SelectedMenuRequest> selectedMenuRequests =
				selectedMenuFactory.getSelectedMenuRequests();

			OrderCreateRequest orderCreateRequest = OrderCreateRequestBuilder.successBuild(
				member.getId(),
				shop.getShopId(),
				selectedMenuRequests
			);
			OrderResponse expectedOrderResponse = getExpectedOrderResponse(orderCreateRequest);

			// when
			OrderResponse actualOrderResponse = orderService.createOrder(orderCreateRequest);

			// then
			assertThat(actualOrderResponse)
				.usingRecursiveComparison()
				.ignoringFields("price")
				.isEqualTo(expectedOrderResponse);
		}
	}
}