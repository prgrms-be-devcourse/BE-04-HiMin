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
import com.prgrms.himin.menu.domain.MenuOptionGroup;
import com.prgrms.himin.menu.domain.MenuOptionGroupRepository;
import com.prgrms.himin.menu.domain.MenuValidator;
import com.prgrms.himin.menu.dto.request.MenuOptionGroupCreateRequest;
import com.prgrms.himin.menu.dto.request.MenuOptionGroupUpdateRequest;
import com.prgrms.himin.menu.dto.response.MenuOptionGroupCreateResponse;
import com.prgrms.himin.setup.domain.MenuOptionGroupSetUp;
import com.prgrms.himin.setup.domain.MenuSetUp;
import com.prgrms.himin.setup.domain.ShopSetUp;
import com.prgrms.himin.setup.request.MenuOptionGroupRequestBuilder;
import com.prgrms.himin.setup.request.MenuOptionGroupUpdateRequestBuilder;
import com.prgrms.himin.shop.domain.Shop;
import com.prgrms.himin.shop.domain.ShopRepository;

@Sql("/truncate.sql")
@SpringBootTest
class MenuOptionGroupServiceTest {

	@Autowired
	MenuService menuService;

	@Autowired
	MenuOptionGroupRepository menuOptionGroupRepository;

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

	Shop shop;

	Menu menu;

	@BeforeEach
	void setUp() {
		shop = shopSetUp.saveOne();
		menu = menuSetUp.saveOne(shop);
	}

	@Nested
	@DisplayName("메뉴 옵션 그룹을 생성할 수 있다.")
	class createMenuOptionGroup {

		@Test
		@DisplayName("성공한다")
		void success_test() {
			// given
			MenuOptionGroupCreateRequest request = MenuOptionGroupRequestBuilder.successBuild();

			// when
			MenuOptionGroupCreateResponse actual = menuService.createMenuOptionGroup(
				shop.getShopId(),
				menu.getId(),
				request
			);

			// then
			MenuOptionGroup findMenuOptionGroup = menuOptionGroupRepository.findById(actual.menuOptionGroupId())
				.get();
			MenuOptionGroupCreateResponse expected = MenuOptionGroupCreateResponse.from(findMenuOptionGroup);
			assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		}

		@Test
		@DisplayName("메뉴가 존재하지 않아서 실패한다.")
		void not_exist_menu_fail_test() {
			// given
			MenuOptionGroupCreateRequest request = MenuOptionGroupRequestBuilder.successBuild();

			Long wrongId = 0L;

			// when & then
			assertThatThrownBy(
				() -> menuService.createMenuOptionGroup(shop.getShopId(), wrongId, request)
			)
				.isInstanceOf(EntityNotFoundException.class);
		}

		@Test
		@DisplayName("가게와 메뉴가 맞지 않아서 실패한다.")
		void not_match_shop_and_menu_fail_test() {
			// given
			Shop anotherShop = shopSetUp.saveOne();
			MenuOptionGroupCreateRequest request = MenuOptionGroupRequestBuilder.successBuild();

			// when & then
			assertThatThrownBy(
				() -> menuService.createMenuOptionGroup(
					anotherShop.getShopId(),
					menu.getId(),
					request
				)
			)
				.isInstanceOf(InvalidValueException.class);
		}
	}

	@Nested
	@DisplayName("메뉴옵셥그룹 업데이트할 수 있다.")
	class updateMenuOptionGroup {

		@Test
		@DisplayName("성공한다.")
		void success_test() {
			// given
			MenuOptionGroup menuOptionGroup = menuOptionGroupSetUp.saveOne(menu);
			MenuOptionGroupUpdateRequest request = MenuOptionGroupUpdateRequestBuilder.successBuild();

			// when
			menuService.updateMenuOptionGroup(
				shop.getShopId(),
				menu.getId(),
				menuOptionGroup.getId(),
				request
			);

			// then
			MenuOptionGroup expected = menuOptionGroupRepository.findById(menuOptionGroup.getId())
				.get();
			assertThat(expected.getName()).isEqualTo(request.name());
		}

		@Test
		@DisplayName("메뉴가 존재하지 않아서 실패한다.")
		void not_exist_menu_fail_test() {
			// given
			MenuOptionGroup menuOptionGroup = menuOptionGroupSetUp.saveOne(menu);
			MenuOptionGroupUpdateRequest request = MenuOptionGroupUpdateRequestBuilder.successBuild();

			Long wrongId = 0L;

			// when & then
			assertThatThrownBy(
				() -> menuService.updateMenuOptionGroup(
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
		void not_exist_menu_option_group_fail_test() {
			// given
			MenuOptionGroupUpdateRequest request = MenuOptionGroupUpdateRequestBuilder.successBuild();

			Long wrongId = 0L;

			// when & then
			assertThatThrownBy(
				() -> menuService.updateMenuOptionGroup(
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
			MenuOptionGroup menuOptionGroup = menuOptionGroupSetUp.saveOne(menu);
			MenuOptionGroupUpdateRequest request = MenuOptionGroupUpdateRequestBuilder.successBuild();

			// when & then
			assertThatThrownBy(
				() -> menuService.updateMenuOptionGroup(
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
			MenuOptionGroupUpdateRequest request = MenuOptionGroupUpdateRequestBuilder.successBuild();

			// when & then
			assertThatThrownBy(
				() -> menuService.updateMenuOptionGroup(
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
	@DisplayName("메뉴옵션그룹 삭제할 수 있다.")
	class deleteMenuOptionGroup {

		@Test
		@DisplayName("성공한다.")
		void success_test() {
			// given
			MenuOptionGroup menuOptionGroup = menuOptionGroupSetUp.saveOne(menu);

			// when
			menuService.deleteMenuOptionGroup(
				shop.getShopId(),
				menu.getId(),
				menuOptionGroup.getId()
			);

			// then
			Optional<MenuOptionGroup> actual = menuOptionGroupRepository.findById(menuOptionGroup.getId());
			assertThat(actual).isEmpty();
		}

		@Test
		@DisplayName("메뉴가 존재하지 않아서 실패한다.")
		void not_exist_menu_fail_test() {
			// given
			MenuOptionGroup menuOptionGroup = menuOptionGroupSetUp.saveOne(menu);

			Long wrongId = 0L;

			// when & then
			assertThatThrownBy(
				() -> menuService.deleteMenuOptionGroup(
					shop.getShopId(),
					wrongId,
					menuOptionGroup.getId()
				)
			)
				.isInstanceOf(EntityNotFoundException.class);
		}

		@Test
		@DisplayName("메뉴옵션그룹이 존재하지 않아서 실패한다.")
		void not_exist_menu_option_group_fail_test() {
			// given
			Long wrongId = 0L;

			// when & then
			assertThatThrownBy(
				() -> menuService.deleteMenuOptionGroup(
					shop.getShopId(),
					menu.getId(),
					wrongId
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

			// when & then
			assertThatThrownBy(
				() -> menuService.deleteMenuOptionGroup(
					anotherShop.getShopId(),
					menu.getId(),
					menuOptionGroup.getId()
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

			// when & then
			assertThatThrownBy(
				() -> menuService.deleteMenuOptionGroup(
					shop.getShopId(),
					menu.getId(),
					menuOptionGroup.getId()
				)
			)
				.isInstanceOf(InvalidValueException.class);
		}
	}
}
