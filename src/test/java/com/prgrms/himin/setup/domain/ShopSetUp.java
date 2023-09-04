package com.prgrms.himin.setup.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

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

	public List<Shop> saveMany(int count) {
		List<Shop> shops = new ArrayList<>();
		IntStream.range(0, count).forEach(i -> shops.add(
			shopRepository.save(buildShop("멕도날드 광명%d동점".formatted(i + 1), Category.FAST_FOOD))
		));

		return shops;
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
			.openingTime("09:00")
			.closingTime("21:00")
			.build();
	}
}
