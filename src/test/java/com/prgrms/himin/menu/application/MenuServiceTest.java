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
import com.prgrms.himin.menu.dto.request.MenuUpdateRequest;
import com.prgrms.himin.menu.dto.response.MenuCreateResponse;
import com.prgrms.himin.menu.dto.response.MenuResponse;
import com.prgrms.himin.setup.request.MenuCreateRequestBuilder;
import com.prgrms.himin.setup.request.MenuUpdateRequestBuilder;
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

	Menu menu;

	@BeforeEach
	void setUp() {
		request = MenuCreateRequestBuilder.successBuild();

		ShopCreateRequest shopCreateRequest = ShopCreateRequestBuilder.successBuild();
		shop = shopCreateRequest.toEntity();

		menu = request.toEntity();
	}

	@Nested
	@DisplayName("메뉴를 생성할 수 있다.")
	class createMenu {

		@Test
		@DisplayName("성공한다")
		void success_test() {
			// given
			given(shopRepository.findById(shop.getShopId())).willReturn(Optional.of(shop));

			given(menuRepository.save(any(Menu.class))).willReturn(menu);

			// when
			MenuCreateResponse result = menuService.createMenu(shop.getShopId(), request);

			// then
			MenuResponse menuResponse = MenuResponse.from(menu);
			assertThat(result).usingRecursiveComparison().isEqualTo(menuResponse);
		}

		@Test
		@DisplayName("가게가 존재하지 않아서 실패한다.")
		void not_exist_shop_fail_test() {
			// given
			Long wrongShopId = 0L;

			given(shopRepository.findById(anyLong()))
				.willReturn(Optional.empty());

			// when & then
			assertThatThrownBy(
				() -> menuService.createMenu(wrongShopId, request)
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
			given(menuRepository.findById(menu.getId()))
				.willReturn(Optional.of(menu));

			// when
			MenuResponse actual = menuService.getMenu(shop.getShopId(), menu.getId());

			// then
			MenuResponse expected = MenuResponse.from(menu);
			assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		}
	}

	@Test
	@DisplayName("메뉴가 존재하지 않아서 실패한다.")
	void not_exist_menu_fail_test() {
		// given
		Long wrongId = 0L;

		given(menuRepository.findById(wrongId))
			.willReturn(Optional.empty());

		// when & then
		assertThatThrownBy(
			() -> menuService.getMenu(shop.getShopId(), wrongId)
		)
			.isInstanceOf(EntityNotFoundException.class);
	}

	@Nested
	@DisplayName("메뉴 정보를 업데이트할 수 있다.")
	class updateMenu {

		@Test
		@DisplayName("성공한다.")
		void success_test() {
			// given
			MenuUpdateRequest.Info updateRequest = MenuUpdateRequestBuilder.infoSuccessBuild();

			given(menuRepository.findById(menu.getId()))
				.willReturn(Optional.of(menu));

			// when
			menuService.updateMenu(shop.getShopId(), menu.getId(), updateRequest);

			// then
			assertThat(menu.getName()).isEqualTo(updateRequest.name());
			assertThat(menu.getPrice()).isEqualTo(updateRequest.price());
		}

		@Test
		@DisplayName("메뉴가 존재하지 않아서 실패한다.")
		void not_exist_menu_fail_test() {
			// given
			Long wrongId = 0L;
			MenuUpdateRequest.Info updateRequest = MenuUpdateRequestBuilder.infoSuccessBuild();

			given(menuRepository.findById(wrongId))
				.willReturn(Optional.empty());

			// when & then
			assertThatThrownBy(
				() -> menuService.updateMenu(shop.getShopId(), wrongId, updateRequest)
			)
				.isInstanceOf(EntityNotFoundException.class);
		}
	}

	@Nested
	@DisplayName("메뉴 상태를 변경할 수 있다.")
	class changeMenuStatus {

		@Test
		@DisplayName("성공한다.")
		void success_test() {
			// given
			MenuUpdateRequest.Status updateStatusRequest = MenuUpdateRequestBuilder.statusSuccessBuild();

			given(menuRepository.findById(menu.getId()))
				.willReturn(Optional.of(menu));

			// when
			menuService.changeMenuStatus(shop.getShopId(), menu.getId(), updateStatusRequest);

			// then
			assertThat(menu.getStatus()).isEqualTo(updateStatusRequest.status());
		}

		@Test
		@DisplayName("메뉴가 존재하지 않아서 실패한다.")
		void not_exist_menu_fail_test() {
			// given
			Long wrongId = 0L;
			MenuUpdateRequest.Status updateStatusRequest = MenuUpdateRequestBuilder.statusSuccessBuild();

			given(menuRepository.findById(wrongId))
				.willReturn(Optional.empty());

			// when & then
			assertThatThrownBy(
				() -> menuService.changeMenuStatus(shop.getShopId(), wrongId, updateStatusRequest)
			)
				.isInstanceOf(EntityNotFoundException.class);
		}
	}

	@Nested
	@DisplayName("메뉴를 삭제할 수 있다.")
	class deleteMenu {

		@Test
		@DisplayName("성공한다.")
		void success_test() {
			// given
			given(menuRepository.findById(menu.getId()))
				.willReturn(Optional.of(menu));

			// when & then
			assertThatCode(
				() -> menuService.deleteMenu(shop.getShopId(), menu.getId())
			)
				.doesNotThrowAnyException();
		}

		@Test
		@DisplayName("메뉴가 존재하지 않아서 실패한다.")
		void not_exist_menu_fail_test() {
			// given
			Long wrongId = 0L;

			given(menuRepository.findById(wrongId))
				.willReturn(Optional.empty());

			// when & then
			assertThatThrownBy(
				() -> menuService.deleteMenu(shop.getShopId(), wrongId)
			)
				.isInstanceOf(EntityNotFoundException.class);

			// verify
			verify(menuRepository, times(1)).findById(anyLong());
			verify(menuRepository, times(0)).delete(any(Menu.class));
		}
	}
}
