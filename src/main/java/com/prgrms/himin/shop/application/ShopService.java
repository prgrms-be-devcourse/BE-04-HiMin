package com.prgrms.himin.shop.application;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.himin.global.error.exception.EntityNotFoundException;
import com.prgrms.himin.global.error.exception.ErrorCode;
import com.prgrms.himin.order.event.CookingFinishedEvent;
import com.prgrms.himin.order.event.StartedCookingEvent;
import com.prgrms.himin.shop.domain.Category;
import com.prgrms.himin.shop.domain.Shop;
import com.prgrms.himin.shop.domain.ShopRepository;
import com.prgrms.himin.shop.domain.ShopSort;
import com.prgrms.himin.shop.domain.ShopStatus;
import com.prgrms.himin.shop.dto.request.ShopCreateRequest;
import com.prgrms.himin.shop.dto.request.ShopSearchCondition;
import com.prgrms.himin.shop.dto.request.ShopUpdateRequest;
import com.prgrms.himin.shop.dto.response.ShopResponse;
import com.prgrms.himin.shop.dto.response.ShopsResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShopService {

	private final ShopRepository shopRepository;

	private final ApplicationEventPublisher publisher;

	@Transactional
	public ShopResponse createShop(ShopCreateRequest request) {
		Shop shop = request.toEntity();
		Shop savedShop = shopRepository.save(shop);

		return ShopResponse.from(savedShop);
	}

	public ShopResponse getShop(Long shopId) {
		Shop shop = shopRepository.findById(shopId)
			.orElseThrow(
				() -> new EntityNotFoundException(ErrorCode.SHOP_NOT_FOUND)
			);

		return ShopResponse.from(shop);
	}

	public ShopsResponse getShops(
		ShopSearchCondition shopSearchCondition,
		int size,
		Long cursor,
		ShopSort sort
	) {
		List<Shop> shops = shopRepository.searchShops(
			shopSearchCondition,
			size,
			cursor,
			sort
		);

		return new ShopsResponse(
			ShopResponse.from(shops),
			size,
			getNextCursor(shops),
			sort,
			isLast(shops, size)
		);
	}

	private Long getNextCursor(List<Shop> shops) {
		if (shops.isEmpty()) {
			return null;
		}

		int lastIndex = getLastIndex(shops);

		return shops.get(lastIndex).getShopId();
	}

	private int getLastIndex(List<Shop> shops) {
		return shops.size() - 1;
	}

	private boolean isLast(List<Shop> shops, int size) {
		if (shops.size() <= size) {
			return true;
		}

		return false;
	}

	@Transactional
	public void updateShop(
		Long shopId,
		ShopUpdateRequest.Info request
	) {
		Shop shop = shopRepository.findById(shopId)
			.orElseThrow(
				() -> new EntityNotFoundException(ErrorCode.SHOP_NOT_FOUND)
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
				() -> new EntityNotFoundException(ErrorCode.SHOP_NOT_FOUND)
			);

		shop.changeStatus(ShopStatus.valueOf(request.status()));
	}

	@Transactional
	public void deleteShop(Long shopId) {
		if (!shopRepository.existsById(shopId)) {
			throw new EntityNotFoundException(ErrorCode.SHOP_NOT_FOUND);
		}

		shopRepository.deleteById(shopId);
	}

	@Transactional
	public void startCooking(
		Long shopId,
		Long orderId
	) {
		publisher.publishEvent(new StartedCookingEvent(shopId, orderId));
	}

	@Transactional
	public void finishCooking(
		Long shopId,
		Long orderId
	) {
		publisher.publishEvent(new CookingFinishedEvent(shopId, orderId));
	}
}
