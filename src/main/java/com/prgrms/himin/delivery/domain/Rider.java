package com.prgrms.himin.delivery.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.prgrms.himin.global.error.exception.ErrorCode;
import com.prgrms.himin.global.error.exception.InvalidValueException;
import com.prgrms.himin.global.util.PhonePolicy;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "riders")
public class Rider {

	private static final int NAME_MAX_LENGTH = 10;

	private static final int PHONE_MAX_LENGTH = 15;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	Long riderId;

	@Column(name = "name", nullable = false)
	String name;

	@Column(name = "phone", nullable = false)
	String phone;

	public Rider(
		String name,
		String phone
	) {
		validateName(name);
		validatePhone(phone);
		this.name = name;
		this.phone = phone;
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
}
