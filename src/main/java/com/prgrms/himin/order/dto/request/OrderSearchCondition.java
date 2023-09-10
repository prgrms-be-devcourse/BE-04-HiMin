package com.prgrms.himin.order.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import com.prgrms.himin.order.domain.OrderStatus;
import com.prgrms.himin.shop.domain.Category;

public class OrderSearchCondition {

	List<Category> categories;

	List<OrderStatus> orderStatuses;

	LocalDateTime startTime;

	LocalDateTime EndTime;
}
