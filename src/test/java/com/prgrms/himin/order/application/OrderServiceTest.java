package com.prgrms.himin.order.application;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import com.prgrms.himin.global.error.exception.EntityNotFoundException;
import com.prgrms.himin.member.domain.Member;
import com.prgrms.himin.order.dto.request.OrderCreateRequest;
import com.prgrms.himin.order.dto.request.OrderSearchCondition;
import com.prgrms.himin.order.dto.request.SelectedMenuOptionRequest;
import com.prgrms.himin.order.dto.request.SelectedMenuRequest;
import com.prgrms.himin.order.dto.response.OrderResponse;
import com.prgrms.himin.order.dto.response.OrderResponses;
import com.prgrms.himin.order.dto.response.SelectedMenuResponse;
import com.prgrms.himin.setup.domain.MemberSetUp;
import com.prgrms.himin.setup.domain.ShopSetUp;
import com.prgrms.himin.setup.factory.SelectedMenuRequestFactory;
import com.prgrms.himin.setup.request.OrderCreateRequestBuilder;
import com.prgrms.himin.shop.domain.Category;
import com.prgrms.himin.shop.domain.Shop;

@SpringBootTest
@Sql("/truncate.sql")
class OrderServiceTest {

	@Autowired
	MemberSetUp memberSetUp;

	@Autowired
	ShopSetUp shopSetUp;

	@Autowired
	OrderService orderService;

	@Autowired
	SelectedMenuRequestFactory selectedMenuRequestFactory;

	@Nested
	@DisplayName("주문 생성을 할 수 있다.")
	class CreateOrder {

		@DisplayName("성공한다.")
		@Test
		void success_test() {
			// given
			Member member = memberSetUp.saveOne();
			Shop shop = shopSetUp.saveOne();

			selectedMenuRequestFactory.initSelectedMenuFactory(shop);
			List<SelectedMenuRequest> selectedMenuRequests = selectedMenuRequestFactory.getSelectedMenuRequests();

			OrderCreateRequest orderCreateRequest = OrderCreateRequestBuilder.successBuild(
				member.getId(),
				shop.getShopId(),
				selectedMenuRequests
			);

			// when
			OrderResponse orderResponse = orderService.createOrder(orderCreateRequest);

			// then
			assertThat(orderResponse.memberId()).isEqualTo(member.getId());
			assertThat(orderResponse.shopId()).isEqualTo(shop.getShopId());
			assertThat(orderResponse.address()).isEqualTo(orderCreateRequest.address());
			assertThat(orderResponse.requirement()).isEqualTo(orderCreateRequest.requirement());

			List<SelectedMenuResponse> selectedMenuResponses = orderResponse.selectedMenus();
			int selectedMenuIdx = 0;
			for (SelectedMenuRequest selectedMenuRequest : selectedMenuRequests) {
				Long menuId = selectedMenuRequest.menuId();
				List<SelectedMenuOptionRequest> selectedMenuOptionRequests =
					selectedMenuRequest.selectedMenuOptions();
				int quantity = selectedMenuRequest.quantity();

				SelectedMenuResponse selectedMenuResponse = selectedMenuResponses.get(selectedMenuIdx);

				List<Long> allMenuOptionIds = new ArrayList<>();
				for (SelectedMenuOptionRequest selectedMenuOptionRequest : selectedMenuOptionRequests) {
					List<Long> menuOptions = selectedMenuOptionRequest.selectedMenuOptions();
					allMenuOptionIds.addAll(menuOptions);
				}

				assertThat(selectedMenuResponse.menuId()).isEqualTo(menuId);
				assertThat(selectedMenuResponse.quantity()).isEqualTo(quantity);
				assertThat(selectedMenuResponse.selectedOptionIds()).isEqualTo(allMenuOptionIds);

				selectedMenuIdx += 1;
			}
		}

		@DisplayName("존재하지 않은 가게 id로 주문 생성에 실패한다.")
		@Test
		void not_found_shop_id_fail_test() {
			// given
			Member member = memberSetUp.saveOne();
			Shop shop = shopSetUp.saveOne();

			Long wrongShopId = 0L;
			selectedMenuRequestFactory.initSelectedMenuFactory(shop);
			List<SelectedMenuRequest> wrongSelectedMenuRequests = selectedMenuRequestFactory
				.getSelectedMenuRequests();

			OrderCreateRequest orderCreateRequest = OrderCreateRequestBuilder.successBuild(
				member.getId(),
				wrongShopId,
				wrongSelectedMenuRequests
			);

			// when && then
			assertThatThrownBy(
				() -> orderService.createOrder(orderCreateRequest)
			).isInstanceOf(EntityNotFoundException.class);
		}
	}

	@Nested
	@DisplayName("주문 검색에 성공 한다.")
	class SearchOrder {
		@DisplayName("성공한다.")
		@Test
		void success_test() {
			// given
			Member member = memberSetUp.saveOne();
			Shop shop = shopSetUp.saveOne();

			selectedMenuRequestFactory.initSelectedMenuFactory(shop);
			List<SelectedMenuRequest> selectedMenuRequests = selectedMenuRequestFactory.getSelectedMenuRequests();

			OrderCreateRequest orderCreateRequest = OrderCreateRequestBuilder.successBuild(
				member.getId(),
				shop.getShopId(),
				selectedMenuRequests
			);

			List<OrderResponse> expectedOrderResponses = new ArrayList<>();
			for (int i = 0; i < 10; i++) {
				OrderResponse orderResponse = orderService.createOrder(orderCreateRequest);
				expectedOrderResponses.add(orderResponse);
			}

			List<Category> categories = List.of(
				shop.getCategory()
			);

			OrderSearchCondition orderSearchCondition = new OrderSearchCondition(
				categories,
				null,
				null,
				null
			);

			int pageSize = 5;

			// when
			OrderResponses orderResponses = orderService.getOrders(
				member.getId(),
				orderSearchCondition,
				pageSize,
				null
			);

			// then
			assertThat(orderResponses.size()).isEqualTo(pageSize);
			assertThat(orderResponses.isLast()).isFalse();

			OrderResponse expectedCursorResponse = expectedOrderResponses.get(pageSize);
			assertThat(orderResponses.nextCursor()).isEqualTo(expectedCursorResponse.orderId());

			int expectedOrderResponsesIdx = 0;
			for (OrderResponse actualOrderResponse : orderResponses.orderResponses()) {
				OrderResponse expectedOrderResponse = expectedOrderResponses.get(expectedOrderResponsesIdx);

				assertThat(actualOrderResponse)
					.usingRecursiveComparison()
					.isEqualTo(expectedOrderResponse);

				expectedOrderResponsesIdx += 1;
			}
		}
	}
}
