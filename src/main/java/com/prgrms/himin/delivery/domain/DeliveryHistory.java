package com.prgrms.himin.delivery.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.prgrms.himin.global.common.BaseEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "delivery_histories")
public class DeliveryHistory extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long deliveryHistoryId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "delivery_id")
	private Delivery delivery;

	@Enumerated(EnumType.STRING)
	private DeliveryStatus deliveryStatus;

	private DeliveryHistory(
		Delivery delivery,
		DeliveryStatus deliveryStatus
	) {
		this.delivery = delivery;
		this.deliveryStatus = deliveryStatus;
	}

	public static DeliveryHistory createBeforeDeliveryHistory(Delivery delivery) {
		return new DeliveryHistory(delivery, DeliveryStatus.BEFORE_DELIVERY);
	}

	public static DeliveryHistory createStartDeliveryHistory(Delivery delivery) {
		return new DeliveryHistory(delivery, DeliveryStatus.DELIVERING);
	}

	public static DeliveryHistory createAllocatedDeliveryHistory(Delivery delivery) {
		return new DeliveryHistory(delivery, DeliveryStatus.ALLOCATED);
	}

	public static DeliveryHistory createArrivedDeliveryHistory(Delivery delivery) {
		return new DeliveryHistory(delivery, DeliveryStatus.ARRIVED);
	}
}
