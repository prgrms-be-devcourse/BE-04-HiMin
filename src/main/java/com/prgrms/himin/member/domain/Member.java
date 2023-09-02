package com.prgrms.himin.member.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.prgrms.himin.global.error.exception.ErrorCode;
import com.prgrms.himin.global.error.exception.InvalidValueException;

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

	@Enumerated(EnumType.STRING)
	@Column(name = "grade", nullable = false)
	private Grade grade;

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Address> addresses = new ArrayList<>();

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

	public void addAddress(Address address) {
		if (addresses.contains(address)) {
			return;
		}
		addresses.add(address);
	}

	public boolean removeAddress(Address address) {
		return addresses.remove(address);
	}

	private void validateLoginId(String loginId) {
		if (loginId == null || loginId.length() > ID_MAX_LENGTH) {
			throw new InvalidValueException(ErrorCode.MEMBER_LOGIN_ID_BAD_REQUEST);
		}
	}

	private void validatePassword(String password) {
		if (password == null || password.length() > PASSWORD_MAX_LENGTH) {
			throw new InvalidValueException(ErrorCode.MEMBER_PASSWORD_BAD_REQUEST);
		}
	}

	private void validateName(String name) {
		if (name == null || name.length() > NAME_MAX_LENGTH) {
			throw new InvalidValueException(ErrorCode.MEMBER_NAME_BAD_REQUEST);
		}
	}

	private void validatePhone(String phone) {
		if (phone == null || phone.length() > PHONE_MAX_LENGTH) {
			throw new InvalidValueException(ErrorCode.MEMBER_PHONE_BAD_REQUEST);
		}
	}
}
