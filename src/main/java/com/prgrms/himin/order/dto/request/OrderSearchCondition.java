package com.prgrms.himin.order.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.prgrms.himin.order.domain.OrderStatus;
import com.prgrms.himin.shop.domain.Category;

public record OrderSearchCondition(

	List<Category> categories,

	List<OrderStatus> orderStatuses,

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	LocalDateTime startTime,

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	LocalDateTime endTime
) {
}
