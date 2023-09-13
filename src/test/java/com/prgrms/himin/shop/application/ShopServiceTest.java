package com.prgrms.himin.shop.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.prgrms.himin.global.error.exception.EntityNotFoundException;
import com.prgrms.himin.setup.request.ShopCreateRequestBuilder;
import com.prgrms.himin.setup.request.ShopUpdateRequestBuilder;
import com.prgrms.himin.shop.domain.Shop;
import com.prgrms.himin.shop.domain.ShopRepository;
import com.prgrms.himin.shop.dto.request.ShopCreateRequest;
import com.prgrms.himin.shop.dto.request.ShopUpdateRequest;
import com.prgrms.himin.shop.dto.response.ShopResponse;

@ExtendWith(MockitoExtension.class)
class ShopServiceTest {

	@Mock
	ShopRepository shopRepository;

	@InjectMocks
	ShopService shopService;

	@Nested
	@DisplayName("가게 생성을 할 수 있다.")
	class CreateShop {

		@DisplayName("성공한다.")
		@Test
		void success_test() {
			// given
			ShopCreateRequest request = ShopCreateRequestBuilder.successBuild();
			Shop shop = request.toEntity();
			given(shopRepository.save(any())).willReturn(shop);

			// when
			ShopResponse actual = shopService.createShop(request);
			ShopResponse expected = ShopResponse.from(shop);

			// then
			assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		}
	}

	@Nested
	@DisplayName("가게 조회를 할 수 있다.")
	class GetShop {

		@DisplayName("성공한다.")
		@Test
		void success_test() {
			// given
			ShopCreateRequest request = ShopCreateRequestBuilder.successBuild();
			Shop shop = request.toEntity();
			given(shopRepository.findById(shop.getShopId())).willReturn(Optional.of(shop));

			// when
			ShopResponse actual = shopService.getShop(shop.getShopId());

			// then
			ShopResponse expected = ShopResponse.from(shop);
			assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		}

		@DisplayName("가게가 존재하지 않아서 실패한다.")
		@Test
		void wrong_request_id_fail_test() {
			Long wrongId = 0L;
			// given
			given(shopRepository.findById(wrongId)).willReturn(Optional.empty());

			// when & then
			assertThatThrownBy(
				() -> shopService.getShop(wrongId)
			)
				.isInstanceOf(EntityNotFoundException.class);
		}
	}

	@Nested
	@DisplayName("가게 업데이트를 할 수 있다.")
	class UpdateShop {

		@DisplayName("성공한다.")
		@Test
		void success_test() {
			// given
			ShopCreateRequest request = ShopCreateRequestBuilder.successBuild();
			Shop shop = request.toEntity();
			given(shopRepository.findById(shop.getShopId())).willReturn(Optional.of(shop));
			ShopUpdateRequest.Info expected = ShopUpdateRequestBuilder.infoSuccessBuild();

			// when
			shopService.updateShop(shop.getShopId(), expected);

			// then
			assertThat(shop.getAddress()).isEqualTo(expected.address());
			assertThat(shop.getCategory().toString()).isEqualTo(expected.category());
			assertThat(shop.getName()).isEqualTo(expected.name());
			assertThat(shop.getPhone()).isEqualTo(expected.phone());
			assertThat(shop.getContent()).isEqualTo(expected.content());
			assertThat(shop.getDeliveryTip()).isEqualTo(expected.deliveryTip());
			assertThat(shop.getOpeningTime()).isEqualTo(expected.openingTime());
			assertThat(shop.getClosingTime()).isEqualTo(expected.closingTime());
		}

		@DisplayName("가게가 존재하지 않아서 실패한다.")
		@Test
		void wrong_request_id_fail_test() {
			// given
			Long wrongId = 0L;
			ShopUpdateRequest.Info failRequest = ShopUpdateRequestBuilder.infoSuccessBuild();
			given(shopRepository.findById(wrongId)).willReturn(Optional.empty());

			// when & then
			assertThatThrownBy(
				() -> shopService.updateShop(wrongId, failRequest)
			)
				.isInstanceOf(EntityNotFoundException.class);
		}
	}

	@Nested
	@DisplayName("가게를 삭제할 수 있다.")
	class DeleteShop {

		@DisplayName("성공한다.")
		@Test
		void success_test() {
			// given
			ShopCreateRequest request = ShopCreateRequestBuilder.successBuild();
			Shop shop = request.toEntity();
			given(shopRepository.existsById(shop.getShopId())).willReturn(true);
			doNothing().when(shopRepository).deleteById(shop.getShopId());

			// when
			shopService.deleteShop(shop.getShopId());

			// then
			assertThatCode(
				() -> shopService.deleteShop(shop.getShopId())
			)
				.doesNotThrowAnyException();
		}

		@DisplayName("가게가 존재하지 않아서 실패한다.")
		@Test
		void wrong_request_id_fail_test() {
			// given
			Long wrongId = 0L;
			given(shopRepository.existsById(wrongId)).willReturn(false);

			// when & then
			assertThatThrownBy(
				() -> shopService.deleteShop(wrongId)
			)
				.isInstanceOf(EntityNotFoundException.class);
			verify(shopRepository, times(1)).existsById(wrongId);
			verify(shopRepository, times(0)).deleteById(wrongId);
		}
	}
}
