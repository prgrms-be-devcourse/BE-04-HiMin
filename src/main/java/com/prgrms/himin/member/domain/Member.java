package com.prgrms.himin.member.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "members")
public class Member {

	private static final int ID_MAX_LENGTH = 20;

	private static final int PASSWORD_MAX_LENGTH = 20;

	private static final int NAME_MAX_LENGTH = 10;

	private static final int PHONE_MAX_LENGTH = 15;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "login_id", nullable = false, length = ID_MAX_LENGTH)
	private String loginId;

	@Column(name = "password", nullable = false, length = PASSWORD_MAX_LENGTH)
	private String password;

	@Column(name = "name", nullable = false, length = NAME_MAX_LENGTH)
	private String name;

	@Column(name = "phone", nullable = false, length = PHONE_MAX_LENGTH)
	private String phone;

	@Column(name = "birthday", nullable = false)
	private LocalDate birthday;

	@Enumerated
	@Column(name = "grade", nullable = false)
	private Grade grade;

	@Builder
	public Member(
		String loginId,
		String password,
		String name,
		String phone,
		LocalDate birthday
	) {
		validateLoginId(loginId);
		validatePassword(password);
		validateName(name);
		validatePhone(phone);
		this.loginId = loginId;
		this.password = password;
		this.name = name;
		this.phone = phone;
		this.birthday = birthday;
		this.grade = Grade.NEW;
	}

	public void updateGrade(Grade grade) {
		this.grade = grade;
	}

	private void validateLoginId(String loginId) {
		if (loginId == null || loginId.length() > ID_MAX_LENGTH) {
			throw new RuntimeException("잘못된 로그인ID입니다.");
		}
	}

	private void validatePassword(String password) {
		if (password == null || password.length() > PASSWORD_MAX_LENGTH) {
			throw new RuntimeException("잘못된 비밀번호입니다.");
		}
	}

	private void validateName(String name) {
		if (name == null || name.length() > NAME_MAX_LENGTH) {
			throw new RuntimeException("잘못된 이름입니다.");
		}
	}

	private void validatePhone(String phone) {
		if (phone == null || phone.length() > PHONE_MAX_LENGTH) {
			throw new RuntimeException("잘못된 핸드폰번호입니다.");
		}
	}
}
