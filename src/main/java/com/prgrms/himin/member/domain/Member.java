package com.prgrms.himin.member.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "members")
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "login_id", nullable = false, length = 20)
	private String loginId;

	@Column(name = "password", nullable = false, length = 60)
	private String password;

	@Column(name = "id", nullable = false, length = 20)
	private String name;

	@Column(name = "id", nullable = false, length = 15)
	private String phone;

	@Column(name = "id", nullable = false)
	private LocalDate birthday;

	@Column(name = "id", nullable = false)
	private Grade grade;
}
