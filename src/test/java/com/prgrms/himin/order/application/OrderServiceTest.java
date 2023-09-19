package com.prgrms.himin.order.application;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.prgrms.himin.global.error.exception.EntityNotFoundException;
import com.prgrms.himin.member.domain.Member;
import com.prgrms.himin.menu.domain.Menu;
import com.prgrms.himin.menu.domain.MenuOption;
import com.prgrms.himin.menu.domain.MenuOptionGroup;
import com.prgrms.himin.order.dto.request.OrderCreateRequest;
import com.prgrms.himin.order.dto.request.SelectedMenuOptionRequest;
import com.prgrms.himin.order.dto.request.SelectedMenuRequest;
import com.prgrms.himin.order.dto.response.OrderResponse;
import com.prgrms.himin.order.dto.response.SelectedMenuResponse;
import com.prgrms.himin.setup.domain.MemberSetUp;
import com.prgrms.himin.setup.domain.MenuOptionGroupSetUp;
import com.prgrms.himin.setup.domain.MenuOptionSetUp;
import com.prgrms.himin.setup.domain.MenuSetUp;
import com.prgrms.himin.setup.domain.ShopSetUp;
import com.prgrms.himin.setup.request.OrderCreateRequestBuilder;
import com.prgrms.himin.setup.request.SelectedMenuOptionRequestBuilder;
import com.prgrms.himin.setup.request.SelectedMenuRequestBuilder;
import com.prgrms.himin.shop.domain.Shop;

@SpringBootTest
class OrderServiceTest {

	@Autowired
	MemberSetUp memberSetUp;

	@Autowired
	ShopSetUp shopSetUp;

	@Autowired
	MenuSetUp menuSetUp;

	@Autowired
	MenuOptionGroupSetUp menuOptionGroupSetUp;

	@Autowired
	MenuOptionSetUp menuOptionSetUp;

	@Autowired
	OrderService orderService;

	@Nested
	@DisplayName("주문 생성을 할 수 있다.")
	class CreateOrder {

		@DisplayName("성공한다.")
		@Test
		void success_test() {
			// given
			Member member = memberSetUp.saveOne();
			Shop shop = shopSetUp.saveOne();

			List<SelectedMenuRequest> selectedMenuRequests = new ArrayList<>();
			for (int i = 0; i < 3; i++) {
				Menu menu = menuSetUp.saveOne(shop);
				List<MenuOptionGroup> menuOptionGroups = menuOptionGroupSetUp.saveMany(menu);

				List<SelectedMenuOptionRequest> selectedMenuOptionRequests = new ArrayList<>();
				for (MenuOptionGroup menuOptionGroup : menuOptionGroups) {
					List<MenuOption> menuOptions = menuOptionSetUp.saveMany(menuOptionGroup);
					SelectedMenuOptionRequest selectedMenuOptionRequest = SelectedMenuOptionRequestBuilder
						.successBuild(menuOptions);
					selectedMenuOptionRequests.add(selectedMenuOptionRequest);
				}

				SelectedMenuRequest selectedMenuRequest = SelectedMenuRequestBuilder.successBuild(
					menu.getId(),
					selectedMenuOptionRequests
				);

				selectedMenuRequests.add(selectedMenuRequest);
			}

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

		@DisplayName("존재하지 않은 메뉴 id로 주문 생성에 실패한다.")
		@Test
		void not_found_menu_id_fail_test() {
			// given
			Member member = memberSetUp.saveOne();
			Shop shop = shopSetUp.saveOne();

			Long wrongMenuId = 0L;
			List<SelectedMenuRequest> wrongSelectedMenuRequests = new ArrayList<>();
			for (int i = 0; i < 3; i++) {
				Menu menu = menuSetUp.saveOne(shop);
				List<MenuOptionGroup> menuOptionGroups = menuOptionGroupSetUp.saveMany(menu);
				List<SelectedMenuOptionRequest> selectedMenuOptionRequests = new ArrayList<>();

				for (MenuOptionGroup menuOptionGroup : menuOptionGroups) {
					List<MenuOption> menuOptions = menuOptionSetUp.saveMany(menuOptionGroup);
					SelectedMenuOptionRequest selectedMenuOptionRequest = SelectedMenuOptionRequestBuilder
						.successBuild(
							menuOptions
						);
					selectedMenuOptionRequests.add(selectedMenuOptionRequest);
				}

				SelectedMenuRequest selectedMenuRequest = SelectedMenuRequestBuilder.successBuild(
					wrongMenuId,
					selectedMenuOptionRequests
				);

				wrongSelectedMenuRequests.add(selectedMenuRequest);
			}

			OrderCreateRequest orderCreateRequest = OrderCreateRequestBuilder.successBuild(
				member.getId(),
				shop.getShopId(),
				wrongSelectedMenuRequests
			);

			// when && then
			assertThatThrownBy(
				() -> orderService.createOrder(orderCreateRequest)
			).isInstanceOf(
				EntityNotFoundException.class
			);
		}

	}
}
