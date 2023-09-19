package com.prgrms.himin.menu.application;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import com.prgrms.himin.global.error.exception.EntityNotFoundException;
import com.prgrms.himin.global.error.exception.InvalidValueException;
import com.prgrms.himin.menu.domain.Menu;
import com.prgrms.himin.menu.domain.MenuOption;
import com.prgrms.himin.menu.domain.MenuOptionGroup;
import com.prgrms.himin.menu.domain.MenuOptionRepository;
import com.prgrms.himin.menu.domain.MenuValidator;
import com.prgrms.himin.menu.dto.request.MenuOptionCreateRequest;
import com.prgrms.himin.menu.dto.request.MenuOptionUpdateRequest;
import com.prgrms.himin.menu.dto.response.MenuOptionCreateResponse;
import com.prgrms.himin.setup.domain.MenuOptionGroupSetUp;
import com.prgrms.himin.setup.domain.MenuOptionSetUp;
import com.prgrms.himin.setup.domain.MenuSetUp;
import com.prgrms.himin.setup.domain.ShopSetUp;
import com.prgrms.himin.setup.request.MenuOptionCreateRequestBuilder;
import com.prgrms.himin.setup.request.MenuOptionUpdateRequestBuilder;
import com.prgrms.himin.shop.domain.Shop;
import com.prgrms.himin.shop.domain.ShopRepository;

@Sql("/truncate.sql")
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
			MenuOptionCreateResponse actual = menuService.createMenuOption(
				shop.getShopId(),
				menu.getId(),
				menuOptionGroup.getId(),
				request
			);

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
				() -> menuService.createMenuOption(
					shop.getShopId(),
					wrongId,
					menuOptionGroup.getId(),
					request
				)
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
				() -> menuService.createMenuOption(
					shop.getShopId(),
					menu.getId(),
					wrongId,
					request
				)
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
				() -> menuService.createMenuOption(
					anotherShop.getShopId(),
					menu.getId(),
					menuOptionGroup.getId(),
					request
				)
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
				() -> menuService.createMenuOption(
					shop.getShopId(),
					menu.getId(),
					menuOptionGroup.getId(),
					request
				)
			)
				.isInstanceOf(InvalidValueException.class);
		}
	}

	@Nested
	@DisplayName("메뉴옵션을 업데이트할 수 있다.")
	class updateMenuOption {

		@Test
		@DisplayName("성공한다.")
		void success_test() {
			// given
			MenuOption menuOption = menuOptionSetUp.saveOne(menuOptionGroup);
			MenuOptionUpdateRequest request = MenuOptionUpdateRequestBuilder.successBuild();

			// when
			menuService.updateMenuOption(
				shop.getShopId(),
				menu.getId(),
				menuOptionGroup.getId(),
				menuOption.getId(),
				request
			);

			// then
			MenuOption expected = menuOptionRepository.findById(menuOption.getId())
				.get();
			assertThat(expected.getName()).isEqualTo(request.name());
			assertThat(expected.getPrice()).isEqualTo(request.price());
		}

		@Test
		@DisplayName("메뉴가 존재하지 않아서 실패한다.")
		void not_exist_menu_fail_test() {
			// given
			MenuOption menuOption = menuOptionSetUp.saveOne(menuOptionGroup);
			MenuOptionUpdateRequest request = MenuOptionUpdateRequestBuilder.successBuild();

			Long wrongId = 0L;

			// when & then
			assertThatThrownBy(
				() -> menuService.updateMenuOption(
					shop.getShopId(),
					wrongId,
					menuOptionGroup.getId(),
					menuOption.getId(),
					request
				)
			)
				.isInstanceOf(EntityNotFoundException.class);
		}

		@Test
		@DisplayName("메뉴옵션그룹이 존재하지 않아서 실패한다.")
		void not_exist_menu_option_group_fail_test() {
			// given
			MenuOption menuOption = menuOptionSetUp.saveOne(menuOptionGroup);
			MenuOptionUpdateRequest request = MenuOptionUpdateRequestBuilder.successBuild();

			Long wrongId = 0L;

			// when & then
			assertThatThrownBy(
				() -> menuService.updateMenuOption(
					shop.getShopId(),
					menu.getId(),
					wrongId,
					menuOption.getId(),
					request
				)
			)
				.isInstanceOf(EntityNotFoundException.class);
		}

		@Test
		@DisplayName("메뉴옵션이 존재하지 않아서 실패한다.")
		void not_exist_menu_option_fail_test() {
			// given
			MenuOptionUpdateRequest request = MenuOptionUpdateRequestBuilder.successBuild();

			Long wrongId = 0L;

			// when & then
			assertThatThrownBy(
				() -> menuService.updateMenuOption(
					shop.getShopId(),
					menu.getId(),
					menuOptionGroup.getId(),
					wrongId,
					request
				)
			)
				.isInstanceOf(EntityNotFoundException.class);
		}

		@Test
		@DisplayName("가게와 메뉴가 맞지 않아서 실패한다.")
		void not_match_shop_and_menu_fail_test() {
			// given
			Shop anotherShop = shopSetUp.saveOne();
			MenuOptionGroup menuOptionGroup = menuOptionGroupSetUp.saveOne(menu);
			MenuOption menuOption = menuOptionSetUp.saveOne(menuOptionGroup);
			MenuOptionUpdateRequest request = MenuOptionUpdateRequestBuilder.successBuild();

			// when & then
			assertThatThrownBy(
				() -> menuService.updateMenuOption(
					anotherShop.getShopId(),
					menu.getId(),
					menuOptionGroup.getId(),
					menuOption.getId(),
					request
				)
			)
				.isInstanceOf(InvalidValueException.class);
		}

		@Test
		@DisplayName("메뉴와 메뉴옵션그룹이 맞지 않아서 실패한다.")
		void not_match_menu_and_menu_option_group_fail_test() {
			// given
			Menu anotherMenu = menuSetUp.saveOne(shop);
			MenuOptionGroup menuOptionGroup = menuOptionGroupSetUp.saveOne(anotherMenu);
			MenuOption menuOption = menuOptionSetUp.saveOne(menuOptionGroup);
			MenuOptionUpdateRequest request = MenuOptionUpdateRequestBuilder.successBuild();

			// when & then
			assertThatThrownBy(
				() -> menuService.updateMenuOption(
					shop.getShopId(),
					menu.getId(),
					menuOptionGroup.getId(),
					menuOption.getId(),
					request
				)
			)
				.isInstanceOf(InvalidValueException.class);
		}

		@Test
		@DisplayName("메뉴옵션그룹과 메뉴옵션이 맞지 않아서 실패한다.")
		void not_match_menu_option_group_and_menu_option_fail_test() {
			// given
			MenuOptionGroup anotherMenuOptionGroup = menuOptionGroupSetUp.saveOne(menu);
			MenuOption menuOption = menuOptionSetUp.saveOne(anotherMenuOptionGroup);
			MenuOptionUpdateRequest request = MenuOptionUpdateRequestBuilder.successBuild();

			// when & then
			assertThatThrownBy(
				() -> menuService.updateMenuOption(
					shop.getShopId(),
					menu.getId(),
					menuOptionGroup.getId(),
					menuOption.getId(),
					request
				)
			)
				.isInstanceOf(InvalidValueException.class);
		}
	}

	@Nested
	@DisplayName("메뉴옵션그룹 삭제할 수 있다.")
	class deleteMenuOption {

		@Test
		@DisplayName("성공한다.")
		void success_test() {
			// given
			MenuOption menuOption = menuOptionSetUp.saveOne(menuOptionGroup);

			// when
			menuService.deleteMenuOption(
				shop.getShopId(),
				menu.getId(),
				menuOptionGroup.getId(),
				menuOption.getId()
			);

			// then
			Optional<MenuOption> actual = menuOptionRepository.findById(menuOption.getId());
			assertThat(actual).isEmpty();
		}

		@Test
		@DisplayName("메뉴가 존재하지 않아서 실패한다.")
		void not_exist_menu_fail_test() {
			// given
			MenuOption menuOption = menuOptionSetUp.saveOne(menuOptionGroup);

			Long wrongId = 0L;

			// when & then
			assertThatThrownBy(
				() -> menuService.deleteMenuOption(
					shop.getShopId(),
					wrongId,
					menuOptionGroup.getId(),
					menuOption.getId()
				)
			)
				.isInstanceOf(EntityNotFoundException.class);
		}

		@Test
		@DisplayName("메뉴옵션그룹이 존재하지 않아서 실패한다.")
		void not_exist_menu_option_group_fail_test() {
			// given
			MenuOption menuOption = menuOptionSetUp.saveOne(menuOptionGroup);
			Long wrongId = 0L;

			// when & then
			assertThatThrownBy(
				() -> menuService.deleteMenuOption(
					shop.getShopId(),
					menu.getId(),
					wrongId,
					menuOption.getId()
				)
			)
				.isInstanceOf(EntityNotFoundException.class);
		}

		@Test
		@DisplayName("가게와 메뉴가 맞지 않아서 실패한다.")
		void not_match_shop_and_menu_fail_test() {
			// given
			Shop anotherShop = shopSetUp.saveOne();
			MenuOptionGroup menuOptionGroup = menuOptionGroupSetUp.saveOne(menu);
			MenuOption menuOption = menuOptionSetUp.saveOne(menuOptionGroup);

			// when & then
			assertThatThrownBy(
				() -> menuService.deleteMenuOption(
					anotherShop.getShopId(),
					menu.getId(),
					menuOptionGroup.getId(),
					menuOption.getId()
				)
			)
				.isInstanceOf(InvalidValueException.class);
		}

		@Test
		@DisplayName("메뉴와 메뉴옵션그룹이 맞지 않아서 실패한다.")
		void not_match_menu_and_menu_option_group_fail_test() {
			// given
			Menu anotherMenu = menuSetUp.saveOne(shop);
			MenuOptionGroup menuOptionGroup = menuOptionGroupSetUp.saveOne(anotherMenu);
			MenuOption menuOption = menuOptionSetUp.saveOne(menuOptionGroup);

			// when & then
			assertThatThrownBy(
				() -> menuService.deleteMenuOption(
					shop.getShopId(),
					menu.getId(),
					menuOptionGroup.getId(),
					menuOption.getId()
				)
			)
				.isInstanceOf(InvalidValueException.class);
		}

		@Test
		@DisplayName("메뉴옵션그룹과 메뉴옵션이 맞지 않아서 실패한다.")
		void not_match_menu_option_group_and_menu_fail_test() {
			// given
			MenuOptionGroup anotherMenuOptionGroup = menuOptionGroupSetUp.saveOne(menu);
			MenuOption menuOption = menuOptionSetUp.saveOne(anotherMenuOptionGroup);

			// when & then
			assertThatThrownBy(
				() -> menuService.deleteMenuOption(
					shop.getShopId(),
					menu.getId(),
					menuOptionGroup.getId(),
					menuOption.getId()
				)
			)
				.isInstanceOf(InvalidValueException.class);
		}
	}
}
