package com.prgrms.himin.delivery.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.prgrms.himin.global.error.exception.BusinessException;
import com.prgrms.himin.global.error.exception.ErrorCode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "deliveries")
public class Delivery {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long deliveryId;

	@Column(name = "order_id")
	private Long orderId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rider_id")
	private Rider rider;

	public Delivery(Long orderId) {
		this.orderId = orderId;
	}

	public void attach(Rider rider) {
		validateRider(rider);
		this.rider = rider;
	}

	private void validateRider(Rider rider) {
		if (rider == null) {
			throw new BusinessException(ErrorCode.DELIVERY_RIDER_BAD_REQUEST);
		}
	}
}
