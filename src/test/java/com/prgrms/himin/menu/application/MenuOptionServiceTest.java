package com.prgrms.himin.menu.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.himin.global.error.exception.EntityNotFoundException;
import com.prgrms.himin.global.error.exception.InvalidValueException;
import com.prgrms.himin.menu.domain.Menu;
import com.prgrms.himin.menu.domain.MenuOption;
import com.prgrms.himin.menu.domain.MenuOptionGroup;
import com.prgrms.himin.menu.domain.MenuOptionRepository;
import com.prgrms.himin.menu.domain.MenuValidator;
import com.prgrms.himin.menu.dto.request.MenuOptionCreateRequest;
import com.prgrms.himin.menu.dto.response.MenuOptionCreateResponse;
import com.prgrms.himin.setup.domain.MenuOptionGroupSetUp;
import com.prgrms.himin.setup.domain.MenuOptionSetUp;
import com.prgrms.himin.setup.domain.MenuSetUp;
import com.prgrms.himin.setup.domain.ShopSetUp;
import com.prgrms.himin.setup.request.MenuOptionCreateRequestBuilder;
import com.prgrms.himin.shop.domain.Shop;
import com.prgrms.himin.shop.domain.ShopRepository;

@Transactional
@SpringBootTest
class MenuOptionServiceTest {

	@Autowired
	MenuService menuService;

	@Autowired
	MenuOptionRepository menuOptionRepository;

	@Autowired
	ShopRepository shopRepository;

	@Autowired
	MenuValidator menuValidator;

	@Autowired
	ShopSetUp shopSetUp;

	@Autowired
	MenuSetUp menuSetUp;

	@Autowired
	MenuOptionGroupSetUp menuOptionGroupSetUp;

	@Autowired
	MenuOptionSetUp menuOptionSetUp;

	Shop shop;

	Menu menu;

	MenuOptionGroup menuOptionGroup;

	@BeforeEach
	void setUp() {
		shop = shopSetUp.saveOne();
		menu = menuSetUp.saveOne(shop);
		menuOptionGroup = menuOptionGroupSetUp.saveOne(menu);
	}

	@Nested
	@DisplayName("메뉴 옵션을 생성할 수 있다.")
	class createMenuOption {

		@Test
		@DisplayName("성공한다")
		void success_test() {
			// given
			MenuOptionCreateRequest request = MenuOptionCreateRequestBuilder.successBuild();

			// when
			MenuOptionCreateResponse actual = menuService.createMenuOption(shop.getShopId(),
				menu.getId(), menuOptionGroup.getId(), request);

			// then
			MenuOption findMenuOption = menuOptionRepository.findById(actual.menuOptionId())
				.get();
			MenuOptionCreateResponse expected = MenuOptionCreateResponse.from(findMenuOption);
			assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		}

		@Test
		@DisplayName("메뉴가 존재하지 않아서 실패한다.")
		void not_exist_menu_fail_test() {
			// given
			MenuOptionCreateRequest request = MenuOptionCreateRequestBuilder.successBuild();

			Long wrongId = 0L;

			// when & then
			assertThatThrownBy(
				() -> menuService.createMenuOption(shop.getShopId(), wrongId, menuOptionGroup.getId(), request)
			)
				.isInstanceOf(EntityNotFoundException.class);
		}

		@Test
		@DisplayName("메뉴옵션그룹이 존재하지 않아서 실패한다.")
		void not_exist_menu_option_fail_test() {
			// given
			MenuOptionCreateRequest request = MenuOptionCreateRequestBuilder.successBuild();

			Long wrongId = 0L;

			// when & then
			assertThatThrownBy(
				() -> menuService.createMenuOption(shop.getShopId(), menu.getId(), wrongId, request)
			)
				.isInstanceOf(EntityNotFoundException.class);
		}

		@Test
		@DisplayName("가게와 메뉴가 맞지 않아서 실패한다.")
		void not_match_shop_and_menu_fail_test() {
			// given
			Shop anotherShop = shopSetUp.saveOne();
			MenuOptionCreateRequest request = MenuOptionCreateRequestBuilder.successBuild();

			// when & then
			assertThatThrownBy(
				() -> menuService.createMenuOption(anotherShop.getShopId(), menu.getId(), menuOptionGroup.getId(),
					request)
			)
				.isInstanceOf(InvalidValueException.class);
		}

		@Test
		@DisplayName("메뉴와 메뉴옵션그룹이 맞지 않아서 실패한다.")
		void not_match_menu_and_menu_option_group_fail_test() {
			// given
			Menu anotherMenu = menuSetUp.saveOne(shop);
			MenuOptionGroup menuOptionGroup = menuOptionGroupSetUp.saveOne(anotherMenu);
			MenuOptionCreateRequest request = MenuOptionCreateRequestBuilder.successBuild();

			// when & then
			assertThatThrownBy(
				() -> menuService.createMenuOption(shop.getShopId(), menu.getId(), menuOptionGroup.getId(),
					request)
			)
				.isInstanceOf(InvalidValueException.class);
		}
	}
}
