package com.prgrms.himin.setup.domain;

import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.prgrms.himin.shop.domain.Category;
import com.prgrms.himin.shop.domain.Shop;
import com.prgrms.himin.shop.domain.ShopRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ShopSetUp {

	private final ShopRepository shopRepository;

	public Shop saveOne() {
		final Shop shop = buildShop("맥도날드", Category.FAST_FOOD);

		return shopRepository.save(shop);
	}

	public List<Shop> saveMany() {
		List<Shop> shops = getShops();

		return shopRepository.saveAll(shops);
	}

	public List<Shop> getShops() {
		final Shop shop1 = buildShop(
			"맥도날드",
			Category.FAST_FOOD,
			3000
		);
		final Shop shop2 = buildShop(
			"롯데리아",
			Category.FAST_FOOD,
			2000
		);

		return List.of(shop1, shop2);
	}

	private Shop buildShop(
		String name,
		Category category
	) {
		return Shop.builder()
			.name(name)
			.category(category)
			.address("경기도 광명시 광명동")
			.phone("02-2611-2222")
			.content("안녕하세요. %s입니다.".formatted(name))
			.deliveryTip(1000)
			.openingTime(LocalTime.of(9, 0))
			.closingTime(LocalTime.of(21, 0))
			.build();
	}

	private Shop buildShop(
		String name,
		Category category,
		int deliveryTip
	) {
		return Shop.builder()
			.name(name)
			.category(category)
			.address("경기도 광명시 광명동")
			.phone("02-2611-2222")
			.content("안녕하세요. %s입니다.".formatted(name))
			.deliveryTip(deliveryTip)
			.openingTime(LocalTime.of(9, 0))
			.closingTime(LocalTime.of(21, 0))
			.build();
	}
}
