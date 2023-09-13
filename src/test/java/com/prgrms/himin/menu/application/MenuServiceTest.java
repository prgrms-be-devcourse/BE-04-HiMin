package com.prgrms.himin.menu.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.prgrms.himin.global.error.exception.EntityNotFoundException;
import com.prgrms.himin.menu.domain.Menu;
import com.prgrms.himin.menu.domain.MenuRepository;
import com.prgrms.himin.menu.domain.MenuValidator;
import com.prgrms.himin.menu.dto.request.MenuCreateRequest;
import com.prgrms.himin.menu.dto.response.MenuCreateResponse;
import com.prgrms.himin.menu.dto.response.MenuResponse;
import com.prgrms.himin.setup.request.MenuCreateRequestBuilder;
import com.prgrms.himin.setup.request.ShopCreateRequestBuilder;
import com.prgrms.himin.shop.domain.Shop;
import com.prgrms.himin.shop.domain.ShopRepository;
import com.prgrms.himin.shop.dto.request.ShopCreateRequest;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

	@InjectMocks
	MenuService menuService;

	@Mock
	MenuRepository menuRepository;

	@Mock
	ShopRepository shopRepository;

	@Mock
	MenuValidator menuValidator;

	MenuCreateRequest request;

	Shop shop;

	@BeforeEach
	void setUp() {
		request = MenuCreateRequestBuilder.successBuild();

		ShopCreateRequest shopCreateRequest = ShopCreateRequestBuilder.successBuild();
		shop = shopCreateRequest.toEntity();
	}

	@Nested
	@DisplayName("메뉴를 생성할 수 있다.")
	class createMenu {

		@Test
		@DisplayName("성공한다")
		void success_test() {
			// given
			Menu menu = request.toEntity();

			given(shopRepository.findById(anyLong()))
				.willReturn(Optional.ofNullable(shop));

			given(menuRepository.save(any(Menu.class)))
				.willReturn(menu);

			// when
			MenuCreateResponse result = menuService.createMenu(anyLong(), request);

			// then
			MenuResponse menuResponse = MenuResponse.from(menu);
			assertThat(result).usingRecursiveComparison().isEqualTo(menuResponse);
		}

		@Test
		@DisplayName("가게가 존재하지 않아서 실패한다.")
		void not_exist_shop_fail_test() {
			// given
			given(shopRepository.findById(anyLong()))
				.willReturn(Optional.empty());

			// when & then
			assertThatThrownBy(
				() -> menuService.createMenu(anyLong(), request)
			)
				.isInstanceOf(EntityNotFoundException.class);

			// verify
			verify(shopRepository, times(1)).findById(anyLong());
			verify(menuRepository, times(0)).save(any(Menu.class));
		}
	}

	@Nested
	@DisplayName("메뉴 조회를 할 수 있다")
	class getMenu {

		@Test
		@DisplayName("성공한다")
		void success_test() {
			// given
			Menu menu = request.toEntity();

			given(menuRepository.findById(menu.getId()))
				.willReturn(Optional.ofNullable(menu));

			// when
			MenuResponse actual = menuService.getMenu(anyLong(), menu.getId());

			// then
			MenuResponse expected = MenuResponse.from(menu);
			assertThat(actual).isEqualTo(expected);
		}
	}
}
