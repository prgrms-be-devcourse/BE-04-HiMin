package com.prgrms.himin.setup.domain;

import org.springframework.stereotype.Component;

import com.prgrms.himin.delivery.domain.Rider;
import com.prgrms.himin.delivery.domain.RiderRepository;
import com.prgrms.himin.delivery.dto.request.RiderCreateRequest;
import com.prgrms.himin.setup.request.RiderCreateRequestBuilder;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RiderSetUp {

	private final RiderRepository riderRepository;

	public Rider saveOne() {
		RiderCreateRequest request = RiderCreateRequestBuilder.successBuild();
		Rider rider = request.toEntity();
		Rider savedRider = riderRepository.save(rider);

		return savedRider;
	}
}
