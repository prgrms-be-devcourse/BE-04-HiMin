package com.prgrms.himin.delivery.application;

import org.springframework.stereotype.Service;

import com.prgrms.himin.delivery.domain.Rider;
import com.prgrms.himin.delivery.domain.RiderRepository;
import com.prgrms.himin.delivery.dto.request.RiderCreateRequest;
import com.prgrms.himin.delivery.dto.response.RiderResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RiderService {

	private final RiderRepository riderRepository;

	public RiderResponse createRider(RiderCreateRequest request) {
		Rider rider = request.toEntity();
		Rider savedRider = riderRepository.save(rider);
		RiderResponse response = RiderResponse.from(savedRider);

		return response;
	}
}
