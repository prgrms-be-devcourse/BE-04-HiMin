package com.prgrms.himin.delivery.api;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.himin.delivery.application.RiderService;
import com.prgrms.himin.delivery.dto.request.RiderCreateRequest;
import com.prgrms.himin.delivery.dto.response.RiderResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/riders")
public class RiderController {

	private final RiderService riderService;

	@PostMapping
	public ResponseEntity<RiderResponse> createRider(@Valid @RequestBody RiderCreateRequest request) {
		RiderResponse response = riderService.createRider(request);

		return ResponseEntity.ok(response);
	}
}
