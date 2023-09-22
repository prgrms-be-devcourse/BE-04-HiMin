package com.prgrms.himin.member.domain;

import static java.util.stream.Collectors.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.prgrms.himin.global.error.exception.ErrorCode;
import com.prgrms.himin.global.error.exception.InvalidValueException;
import com.prgrms.himin.global.util.PhonePolicy;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "members")
public class Member {

	public static final int ID_MAX_LENGTH = 20;

	public static final int PASSWORD_MAX_LENGTH = 20;

	private static final int ENCODED_PASSWORD_LENGTH = 60;

	public static final int NAME_MAX_LENGTH = 10;

	public static final int PHONE_MAX_LENGTH = 15;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "login_id", nullable = false, length = ID_MAX_LENGTH)
	private String loginId;

	@Column(name = "password", nullable = false, length = ENCODED_PASSWORD_LENGTH)
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

	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> roles = new ArrayList<>();

	@Builder
	public Member(
		String loginId,
		String password,
		String name,
		String phone,
		LocalDate birthday
	) {
		validateLoginId(loginId);
		validateName(name);
		validatePhone(phone);
		validateBirthDay(birthday);
		this.loginId = loginId;
		this.password = password;
		this.name = name;
		this.phone = phone;
		this.birthday = birthday;
		this.grade = Grade.NEW;
		this.roles.add(Permission.ROLE_USER.name());
	}

	public static String password(String password, String encodedPassword) {
		validatePassword(password);
		return encodedPassword;
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

	public List<GrantedAuthority> getAuthorities() {
		return this.roles.stream()
			.map(SimpleGrantedAuthority::new)
			.collect(toList());
	}

	public void checkPassword(
		PasswordEncoder passwordEncoder,
		String credentials
	) {
		if (!passwordEncoder.matches(credentials, this.password)) {
			throw new InvalidValueException(ErrorCode.MEMBER_LOGIN_FAIL);
		}
	}

	public void updateInfo(
		String loginId,
		String password,
		String name,
		String phone,
		LocalDate birthday
	) {
		validateLoginId(loginId);
		validateName(name);
		validatePhone(phone);
		validateBirthDay(birthday);
		this.loginId = loginId;
		this.password = password;
		this.name = name;
		this.phone = phone;
		this.birthday = birthday;
	}

	private void validateLoginId(String loginId) {
		if (loginId == null || loginId.length() > ID_MAX_LENGTH) {
			throw new InvalidValueException(ErrorCode.MEMBER_LOGIN_ID_BAD_REQUEST);
		}
	}

	private static void validatePassword(String password) {
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
		if (phone == null || phone.length() > PHONE_MAX_LENGTH || !PhonePolicy.matches(phone)) {
			throw new InvalidValueException(ErrorCode.MEMBER_PHONE_BAD_REQUEST);
		}
	}

	private void validateBirthDay(LocalDate birthday) {
		if (birthday == null || birthday.isAfter(LocalDate.now())) {
			throw new InvalidValueException(ErrorCode.MEMBER_BIRTHDAY_BAD_REQUEST);
		}
	}
}
