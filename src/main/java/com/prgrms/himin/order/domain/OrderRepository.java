package com.prgrms.himin.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

	Order findFirstByMemberIdOrderByOrderIdDesc(Long member_id);
}
