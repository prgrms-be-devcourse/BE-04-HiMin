package com.prgrms.himin.order.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.himin.member.domain.Member;
import com.prgrms.himin.menu.domain.Menu;
import com.prgrms.himin.menu.domain.MenuOption;
import com.prgrms.himin.menu.domain.MenuOptionGroup;
import com.prgrms.himin.order.domain.OrderItem;
import com.prgrms.himin.order.domain.SelectedOption;
import com.prgrms.himin.order.dto.request.OrderCreateRequest;
import com.prgrms.himin.order.dto.request.SelectedMenuOptionRequest;
import com.prgrms.himin.order.dto.request.SelectedMenuRequest;
import com.prgrms.himin.setup.domain.MemberSetUp;
import com.prgrms.himin.setup.domain.MenuOptionGroupSetUp;
import com.prgrms.himin.setup.domain.MenuOptionSetUp;
import com.prgrms.himin.setup.domain.MenuSetUp;
import com.prgrms.himin.setup.domain.OrderItemSetUp;
import com.prgrms.himin.setup.domain.ShopSetUp;
import com.prgrms.himin.setup.request.OrderCreateRequestBuilder;
import com.prgrms.himin.setup.request.SelectedMenuOptionRequestBuilder;
import com.prgrms.himin.setup.request.SelectedMenuRequestBuilder;
import com.prgrms.himin.shop.domain.Shop;

@AutoConfigureMockMvc
@SpringBootTest
class OrderControllerTest {

	final String BASE_URL = "/api/orders";

	@Autowired
	MockMvc mvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	MemberSetUp memberSetUp;

	@Autowired
	MenuSetUp menuSetUp;

	@Autowired
	MenuOptionGroupSetUp menuOptionGroupSetUp;

	@Autowired
	MenuOptionSetUp menuOptionSetUp;

	@Autowired
	ShopSetUp shopSetUp;

	@Autowired
	OrderItemSetUp orderItemSetUp;

	@Nested
	@DisplayName("주문 생성을 할 수 있다.")
	class CreateOrder {

		private int calculateMenuPrice(Menu menu, int quantity) {
			return menu.getPrice() * quantity;
		}

		private int calculateMenuOptionPrice(MenuOption menuOption, int quantity) {
			return menuOption.getPrice() * quantity;
		}

		@DisplayName("성공한다.")
		@Test
		void success_test() throws Exception {
			// given
			Member member = memberSetUp.saveOne();
			Shop shop = shopSetUp.saveOne();
			Menu menu1 = menuSetUp.saveOne(shop);
			Menu menu2 = menuSetUp.saveOne(shop);

			// 옵션그룹 3개
			List<MenuOptionGroup> menuOptionGroup1 = menuOptionGroupSetUp.saveMany(menu1);
			List<MenuOptionGroup> menuOptionGroup2 = menuOptionGroupSetUp.saveMany(menu2);

			// 옵션그룹 한개당 옵션 6개
			List<MenuOption> menuOptions1 = menuOptionSetUp.saveMany(menuOptionGroup1.get(0));
			List<MenuOption> menuOptions2 = menuOptionSetUp.saveMany(menuOptionGroup1.get(1));
			List<MenuOption> menuOptions3 = menuOptionSetUp.saveMany(menuOptionGroup1.get(2));

			List<MenuOption> menuOptions4 = menuOptionSetUp.saveMany(menuOptionGroup2.get(0));
			List<MenuOption> menuOptions5 = menuOptionSetUp.saveMany(menuOptionGroup2.get(1));
			List<MenuOption> menuOptions6 = menuOptionSetUp.saveMany(menuOptionGroup2.get(2));

			SelectedMenuOptionRequest menuOptionRequest1 = SelectedMenuOptionRequestBuilder
				.successBuild(menuOptions1);

			SelectedMenuOptionRequest menuOptionRequest2 = SelectedMenuOptionRequestBuilder
				.successBuild(menuOptions2);

			SelectedMenuOptionRequest menuOptionRequest3 = SelectedMenuOptionRequestBuilder
				.successBuild(menuOptions3);

			List<SelectedMenuOptionRequest> selectedMenuOptions1 = List.of(
				menuOptionRequest1,
				menuOptionRequest2,
				menuOptionRequest3
			);

			SelectedMenuRequest selectedMenuRequest1 = SelectedMenuRequestBuilder.successBuild(
				menu1.getId(),
				selectedMenuOptions1
			);

			SelectedMenuOptionRequest menuOptionRequest4 = SelectedMenuOptionRequestBuilder
				.successBuild(menuOptions4);

			SelectedMenuOptionRequest menuOptionRequest5 = SelectedMenuOptionRequestBuilder
				.successBuild(menuOptions5);

			SelectedMenuOptionRequest menuOptionRequest6 = SelectedMenuOptionRequestBuilder
				.successBuild(menuOptions6);

			List<SelectedMenuOptionRequest> selectedMenuOptions2 = List.of(
				menuOptionRequest4,
				menuOptionRequest5,
				menuOptionRequest6
			);

			SelectedMenuRequest selectedMenuRequest2 = SelectedMenuRequestBuilder.successBuild(
				menu2.getId(),
				selectedMenuOptions2
			);

			List<SelectedMenuRequest> selectedMenuRequests = List.of(
				selectedMenuRequest1,
				selectedMenuRequest2
			);

			List<SelectedOption> selectedOptions1 = SelectedOption.from(menuOptions1);
			List<SelectedOption> selectedOptions2 = SelectedOption.from(menuOptions2);
			List<SelectedOption> selectedOptions3 = SelectedOption.from(menuOptions3);
			List<SelectedOption> selectedOptions4 = SelectedOption.from(menuOptions4);
			List<SelectedOption> selectedOptions5 = SelectedOption.from(menuOptions5);
			List<SelectedOption> selectedOptions6 = SelectedOption.from(menuOptions6);

			OrderItem orderItem1 = orderItemSetUp.makeOne(
				menu1,
				selectedMenuRequest1.quantity()
			);
			selectedOptions1.forEach(selectedOption -> selectedOption.attachTo(orderItem1));
			selectedOptions2.forEach(selectedOption -> selectedOption.attachTo(orderItem1));
			selectedOptions3.forEach(selectedOption -> selectedOption.attachTo(orderItem1));

			OrderItem orderItem2 = orderItemSetUp.makeOne(
				menu2,
				selectedMenuRequest2.quantity()
			);
			selectedOptions4.forEach(selectedOption -> selectedOption.attachTo(orderItem2));
			selectedOptions5.forEach(selectedOption -> selectedOption.attachTo(orderItem2));
			selectedOptions6.forEach(selectedOption -> selectedOption.attachTo(orderItem2));

			int expectedPrice = orderItem1.calculateOrderItemPrice()
				+ orderItem2.calculateOrderItemPrice();

			OrderCreateRequest request = OrderCreateRequestBuilder.successBuild(
				member.getId(),
				shop.getShopId(),
				selectedMenuRequests
			);

			String body = objectMapper.writeValueAsString(request);

			// when
			ResultActions resultActions = mvc.perform(post(BASE_URL)
				.content(body)
				.contentType(MediaType.APPLICATION_JSON));

			// then
			resultActions.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("orderId").isNotEmpty())
				.andExpect(jsonPath("memberId").value(member.getId()))
				.andExpect(jsonPath("shopId").value(shop.getShopId()))
				.andExpect(jsonPath("address").value(request.address()))
				.andExpect(jsonPath("requirement").value(request.requirement()))
				.andExpect(jsonPath("selectedMenus[0].menuId").value(menu1.getId()))
				.andExpect(jsonPath("selectedMenus[0].quantity").value(selectedMenuRequest1.quantity()))
				.andExpect(jsonPath("selectedMenus[0].selectedOptionIds[0]")
					.value(menuOptions1.get(0).getId()))
				.andExpect(jsonPath("selectedMenus[0].selectedOptionIds[1]")
					.value(menuOptions1.get(1).getId()))
				.andExpect(jsonPath("selectedMenus[0].selectedOptionIds[2]")
					.value(menuOptions1.get(2).getId()))
				.andExpect(jsonPath("selectedMenus[0].selectedOptionIds[3]")
					.value(menuOptions1.get(3).getId()))
				.andExpect(jsonPath("selectedMenus[0].selectedOptionIds[4]")
					.value(menuOptions1.get(4).getId()))
				.andExpect(jsonPath("selectedMenus[0].selectedOptionIds[5]")
					.value(menuOptions1.get(5).getId()))
				.andExpect(jsonPath("selectedMenus[0].selectedOptionIds[6]")
					.value(menuOptions2.get(0).getId()))
				.andExpect(jsonPath("selectedMenus[0].selectedOptionIds[7]")
					.value(menuOptions2.get(1).getId()))
				.andExpect(jsonPath("selectedMenus[0].selectedOptionIds[8]")
					.value(menuOptions2.get(2).getId()))
				.andExpect(jsonPath("selectedMenus[0].selectedOptionIds[9]")
					.value(menuOptions2.get(3).getId()))
				.andExpect(jsonPath("selectedMenus[0].selectedOptionIds[10]")
					.value(menuOptions2.get(4).getId()))
				.andExpect(jsonPath("selectedMenus[0].selectedOptionIds[11]")
					.value(menuOptions2.get(5).getId()))
				.andExpect(jsonPath("selectedMenus[0].selectedOptionIds[12]")
					.value(menuOptions3.get(0).getId()))
				.andExpect(jsonPath("selectedMenus[0].selectedOptionIds[13]")
					.value(menuOptions3.get(1).getId()))
				.andExpect(jsonPath("selectedMenus[0].selectedOptionIds[14]")
					.value(menuOptions3.get(2).getId()))
				.andExpect(jsonPath("selectedMenus[0].selectedOptionIds[15]")
					.value(menuOptions3.get(3).getId()))
				.andExpect(jsonPath("selectedMenus[0].selectedOptionIds[16]")
					.value(menuOptions3.get(4).getId()))
				.andExpect(jsonPath("selectedMenus[0].selectedOptionIds[17]")
					.value(menuOptions3.get(5).getId()))
				.andExpect(jsonPath("selectedMenus[1].selectedOptionIds[0]")
					.value(menuOptions4.get(0).getId()))
				.andExpect(jsonPath("selectedMenus[1].selectedOptionIds[1]")
					.value(menuOptions4.get(1).getId()))
				.andExpect(jsonPath("selectedMenus[1].selectedOptionIds[2]")
					.value(menuOptions4.get(2).getId()))
				.andExpect(jsonPath("selectedMenus[1].selectedOptionIds[3]")
					.value(menuOptions4.get(3).getId()))
				.andExpect(jsonPath("selectedMenus[1].selectedOptionIds[4]")
					.value(menuOptions4.get(4).getId()))
				.andExpect(jsonPath("selectedMenus[1].selectedOptionIds[5]")
					.value(menuOptions4.get(5).getId()))
				.andExpect(jsonPath("selectedMenus[1].selectedOptionIds[6]")
					.value(menuOptions5.get(0).getId()))
				.andExpect(jsonPath("selectedMenus[1].selectedOptionIds[7]")
					.value(menuOptions5.get(1).getId()))
				.andExpect(jsonPath("selectedMenus[1].selectedOptionIds[8]")
					.value(menuOptions5.get(2).getId()))
				.andExpect(jsonPath("selectedMenus[1].selectedOptionIds[9]")
					.value(menuOptions5.get(3).getId()))
				.andExpect(jsonPath("selectedMenus[1].selectedOptionIds[10]")
					.value(menuOptions5.get(4).getId()))
				.andExpect(jsonPath("selectedMenus[1].selectedOptionIds[11]")
					.value(menuOptions5.get(5).getId()))
				.andExpect(jsonPath("selectedMenus[1].selectedOptionIds[12]")
					.value(menuOptions6.get(0).getId()))
				.andExpect(jsonPath("selectedMenus[1].selectedOptionIds[13]")
					.value(menuOptions6.get(1).getId()))
				.andExpect(jsonPath("selectedMenus[1].selectedOptionIds[14]")
					.value(menuOptions6.get(2).getId()))
				.andExpect(jsonPath("selectedMenus[1].selectedOptionIds[15]")
					.value(menuOptions6.get(3).getId()))
				.andExpect(jsonPath("selectedMenus[1].selectedOptionIds[16]")
					.value(menuOptions6.get(4).getId()))
				.andExpect(jsonPath("selectedMenus[1].selectedOptionIds[17]")
					.value(menuOptions6.get(5).getId()))
				.andExpect(jsonPath("price").value(expectedPrice))
				.andDo(print());
		}
	}
}
