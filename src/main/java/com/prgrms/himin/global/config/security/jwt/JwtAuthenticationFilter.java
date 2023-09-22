package com.prgrms.himin.global.config.security.jwt;

import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static org.apache.logging.log4j.util.Strings.*;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

	private final String headerKey;

	private final Jwt jwt;

	@Override
	public void doFilter(
		ServletRequest request,
		ServletResponse response,
		FilterChain chain
	) throws IOException, ServletException {

		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		HttpServletResponse httpServletResponse = (HttpServletResponse)response;

		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			String token = getToken(httpServletRequest);
			if (token != null) {
				try {
					Jwt.Claims claims = verify(token);
					log.debug("JWT 분석 결과: {}", claims);

					String username = claims.getUsername();
					List<GrantedAuthority> authorities = getAuthorities(claims);

					if (isNotEmpty(username) && authorities.size() > 0) {
						JwtAuthenticationToken authentication = new JwtAuthenticationToken(
							new JwtAuthentication(token, username),
							null,
							authorities
						);
						SecurityContextHolder.getContext()
							.setAuthentication(authentication);
					}
				} catch (Exception e) {
					log.warn("JWT 처리 실패: {}", e.getMessage());
				}
			}
		} else {
			log.debug("SecurityContextHolder에 이미 Authentication가 설정되어 있습니다. '{}'",
				SecurityContextHolder.getContext()
					.getAuthentication());
		}

		chain.doFilter(httpServletRequest, httpServletResponse);
	}

	private String getToken(HttpServletRequest request) {
		String token = request.getHeader(headerKey);
		if (isNotEmpty(token)) {
			log.debug("JWT 인증 API 감지: {}", token);
			return URLDecoder.decode(token, StandardCharsets.UTF_8);
		}

		return null;
	}

	private Jwt.Claims verify(String token) {
		return jwt.verify(token);
	}

	private List<GrantedAuthority> getAuthorities(Jwt.Claims claims) {
		String[] roles = claims.getRoles();
		if (roles == null || roles.length == 0) {
			return emptyList();
		}

		return Arrays.stream(roles)
			.map(SimpleGrantedAuthority::new)
			.collect(toList());
	}
}
