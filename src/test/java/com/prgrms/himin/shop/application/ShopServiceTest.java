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
import com.prgrms.himin.shop.domain.Shop;
import com.prgrms.himin.shop.domain.ShopRepository;
import com.prgrms.himin.shop.dto.request.ShopCreateRequest;
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
			given(shopRepository.findById(anyLong())).willReturn(Optional.of(shop));

			// when
			ShopResponse actual = shopService.getShop(anyLong());

			// then
			ShopResponse expected = ShopResponse.from(shop);
			assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		}

		@DisplayName("가게가 존재하지 않아서 실패한다.")
		@Test
		void wrong_request_id_fail_test() {
			// given
			given(shopRepository.findById(anyLong())).willReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() -> shopService.getShop(anyLong()))
				.isInstanceOf(EntityNotFoundException.class);
		}
	}
}
