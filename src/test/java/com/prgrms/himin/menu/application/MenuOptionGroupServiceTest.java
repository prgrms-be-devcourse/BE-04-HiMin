package com.prgrms.himin.menu.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.prgrms.himin.global.error.exception.EntityNotFoundException;
import com.prgrms.himin.global.error.exception.InvalidValueException;
import com.prgrms.himin.menu.domain.Menu;
import com.prgrms.himin.menu.domain.MenuOptionGroup;
import com.prgrms.himin.menu.domain.MenuOptionGroupRepository;
import com.prgrms.himin.menu.domain.MenuValidator;
import com.prgrms.himin.menu.dto.request.MenuOptionGroupCreateRequest;
import com.prgrms.himin.menu.dto.response.MenuOptionGroupCreateResponse;
import com.prgrms.himin.setup.domain.MenuOptionGroupSetUp;
import com.prgrms.himin.setup.domain.MenuSetUp;
import com.prgrms.himin.setup.domain.ShopSetUp;
import com.prgrms.himin.setup.request.MenuOptionGroupRequestBuilder;
import com.prgrms.himin.shop.domain.Shop;
import com.prgrms.himin.shop.domain.ShopRepository;

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
			MenuOptionGroupCreateResponse actual = menuService.createMenuOptionGroup(shop.getShopId(),
				menu.getId(), request);

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
				() -> menuService.createMenuOptionGroup(anotherShop.getShopId(), menu.getId(), request)
			)
				.isInstanceOf(InvalidValueException.class);
		}
	}
}
