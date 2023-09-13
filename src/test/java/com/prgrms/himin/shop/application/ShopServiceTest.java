package com.prgrms.himin.shop.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
			when(shopRepository.save(any())).thenReturn(shop);

			// when
			ShopResponse savedShop = shopService.createShop(request);

			// then
			assertThat(savedShop).usingRecursiveComparison().isEqualTo(shop);
		}
	}
}
