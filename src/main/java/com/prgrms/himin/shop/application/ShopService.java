package com.prgrms.himin.shop.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.himin.shop.domain.Category;
import com.prgrms.himin.shop.domain.Shop;
import com.prgrms.himin.shop.domain.ShopRepository;
import com.prgrms.himin.shop.dto.request.ShopCreateRequest;
import com.prgrms.himin.shop.dto.request.ShopUpdateRequest;
import com.prgrms.himin.shop.dto.response.ShopResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShopService {

	private final ShopRepository shopRepository;

	@Transactional
	public ShopResponse createShop(ShopCreateRequest request) {
		Shop shop = request.toEntity();
		Shop savedShop = shopRepository.save(shop);

		return ShopResponse.from(savedShop);
	}

	public ShopResponse getShop(Long shopId) {
		Shop shop = shopRepository.findById(shopId)
			.orElseThrow(
				() -> new RuntimeException("가게를 찾을 수 없습니다.")
			);

		return ShopResponse.from(shop);
	}

	public List<ShopResponse> getShops(
		String name,
		Category category,
		String address,
		Integer deliveryTip
	) {
		List<Shop> shops = shopRepository.searchShops(
			name,
			category,
			address,
			deliveryTip
		);

		return shops.stream()
			.map(ShopResponse::from)
			.collect(Collectors.toList());
	}

	@Transactional
	public void updateShop(
		Long shopId,
		ShopUpdateRequest.Info request
	) {
		Shop shop = shopRepository.findById(shopId)
			.orElseThrow(
				() -> new RuntimeException("가게를 찾을 수 없습니다.")
			);

		shop.updateInfo(
			request.name(),
			Category.valueOf(request.category()),
			request.address(),
			request.phone(),
			request.content(),
			request.deliveryTip(),
			request.openingTime(),
			request.closingTime()
		);
	}

	@Transactional
	public void changeShopStatus(
		Long shopId,
		ShopUpdateRequest.Status request
	) {
		Shop shop = shopRepository.findById(shopId)
			.orElseThrow(
				() -> new RuntimeException("가게를 찾을 수 없습니다.")
			);

		shop.changeStatus(request.status());
	}

	@Transactional
	public void deleteShop(Long shopId) {
		if (!shopRepository.existsById(shopId)) {
			throw new RuntimeException("가게를 찾을 수 없습니다.");
		}
		
		shopRepository.deleteById(shopId);
	}
}
