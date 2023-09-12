package com.prgrms.himin.global.config.security.jwt;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import com.prgrms.himin.member.application.MemberService;
import com.prgrms.himin.member.domain.Member;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

	private final Jwt jwt;

	private final MemberService memberService;

	@Override
	public boolean supports(Class<?> authentication) {
		return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		JwtAuthenticationToken jwtAuthentication = (JwtAuthenticationToken)authentication;
		
		return processUserAuthentication(
			String.valueOf(jwtAuthentication.getPrincipal()),
			jwtAuthentication.getCredentials()
		);
	}

	private Authentication processUserAuthentication(
		String principal,
		String credentials
	) {
		try {
			Member member = memberService.login(
				principal,
				credentials
			);
			List<GrantedAuthority> authorities = member.getAuthorities();
			String token = getToken(
				member.getLoginId(),
				authorities
			);
			JwtAuthenticationToken authentication = new JwtAuthenticationToken(
				new JwtAuthentication(
					token,
					member.getLoginId()
				),
				null,
				authorities);
			authentication.setDetails(member);

			return authentication;

		} catch (IllegalArgumentException e) {
			throw new BadCredentialsException(e.getMessage());
		} catch (DataAccessException e) {
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
	}

	private String getToken(
		String username,
		List<GrantedAuthority> authorities
	) {
		String[] roles = authorities.stream()
			.map(GrantedAuthority::getAuthority)
			.toArray(String[]::new);

		return jwt.sign(Jwt.Claims.of(
			username,
			roles
		));
	}
}