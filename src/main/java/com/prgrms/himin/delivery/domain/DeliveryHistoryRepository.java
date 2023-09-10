package com.prgrms.himin.delivery.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeliveryHistoryRepository extends JpaRepository<DeliveryHistory, Long> {

	@Query(value = "SELECT d FROM DeliveryHistory as d WHERE d.delivery.deliveryId = :deliveryId")
	List<DeliveryHistory> findDeliveryHistoriesByDeliveryId(@Param("deliveryId") Long deliveryId);
}
