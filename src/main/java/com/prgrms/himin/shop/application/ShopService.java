package com.prgrms.himin.shop.application;

import com.prgrms.himin.shop.domain.Shop;
import com.prgrms.himin.shop.domain.ShopRepository;
import com.prgrms.himin.shop.dto.request.ShopCreateRequest;
import com.prgrms.himin.shop.dto.request.ShopUpdateRequest;
import com.prgrms.himin.shop.dto.response.ShopResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .orElseThrow(() -> new RuntimeException("가게를 찾을 수 없습니다."));
        return ShopResponse.from(shop);
    }

    @Transactional
    public void updateShop(Long shopId, ShopUpdateRequest request) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("가게를 찾을 수 없습니다."));

        shop.updateInfo(
                request.getName(),
                request.getCategory(),
                request.getAddress(),
                request.getPhone(),
                request.getContent(),
                request.getDeliveryTip(),
                request.getOpeningTime(),
                request.getClosingTime()
        );
    }
}
