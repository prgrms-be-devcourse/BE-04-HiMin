package com.prgrms.himin.menu.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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
}
