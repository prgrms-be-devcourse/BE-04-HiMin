package com.prgrms.himin.global.config.security.jwt;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

	private final Object principal;

	private String credentials;

	public JwtAuthenticationToken(
		String principal,
		String credentials
	) {
		super(null);
		super.setAuthenticated(false);
		this.principal = principal;
		this.credentials = credentials;
	}

	JwtAuthenticationToken(
		Object principal,
		String credentials,
		Collection<? extends GrantedAuthority> authorities
	) {
		super(authorities);
		super.setAuthenticated(true);
		this.principal = principal;
		this.credentials = credentials;
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}

	@Override
	public String getCredentials() {
		return credentials;
	}

	@Override
	public void setAuthenticated(boolean authenticated) {
		if (authenticated) {
			throw new IllegalArgumentException(
				"이 토큰의 인증을 허용할 수 없습니다. 대신 생성자를 사용하세요.");
		}
		super.setAuthenticated(false);
	}

	@Override
	public void eraseCredentials() {
		super.eraseCredentials();
		credentials = null;
	}
}
