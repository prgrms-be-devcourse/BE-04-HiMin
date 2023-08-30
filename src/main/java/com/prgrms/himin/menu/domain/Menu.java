package com.prgrms.himin.menu.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.prgrms.himin.order.domain.Order;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "menus")
public class Menu {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name", nullable = false, length = 20)
	private String name;

	@Column(name = "price", nullable = false)
	private int price;

	@Column(name = "popularity", nullable = false, columnDefinition = "BIT(1)")
	private boolean popularity;

	@Enumerated
	@Column(name = "status", nullable = false)
	private MenuStatus status;

	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;

	@Builder
	public Menu(
		String name,
		int price,
		boolean popularity
	) {
		validateName(name);
		validatePrice(price);
		this.name = name;
		this.price = price;
		this.popularity = popularity;
		this.status = MenuStatus.unsellable;
	}

	private void validatePrice(int price) {
		if (price < 0) {
			throw new RuntimeException("price는 0 이상이어야 합니다.");
		}
	}

	private void validateName(String name) {
		if (name == null) {
			throw new RuntimeException("name은 null값이면 안됩니다.");
		}
	}
}
