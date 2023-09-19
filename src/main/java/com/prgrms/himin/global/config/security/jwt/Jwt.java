package com.prgrms.himin.global.config.security.jwt;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.Getter;
import lombok.ToString;

public final class Jwt {

	private final String issuer;

	private final String clientSecret;

	private final int expirySeconds;

	private final Algorithm algorithm;

	private final JWTVerifier jwtVerifier;

	public Jwt(
		String issuer,
		String clientSecret,
		int expirySeconds
	) {
		this.issuer = issuer;
		this.clientSecret = clientSecret;
		this.expirySeconds = expirySeconds;
		this.algorithm = Algorithm.HMAC512(clientSecret);
		this.jwtVerifier = JWT.require(algorithm)
			.withIssuer(issuer)
			.build();
	}

	public String sign(Claims claims) {
		Date now = new Date();
		JWTCreator.Builder builder = JWT.create();
		builder.withIssuer(issuer);
		builder.withIssuedAt(now);
		if (expirySeconds > 0) {
			builder.withExpiresAt(new Date(now.getTime() + expirySeconds * 1000L));
		}
		builder.withClaim("username", claims.username);
		builder.withArrayClaim("roles", claims.roles);
		return builder.sign(algorithm);
	}

	public Claims verify(String token) throws JWTVerificationException {
		return new Claims(jwtVerifier.verify(token));
	}

	@Getter
	@ToString
	public static class Claims {

		private String username;

		private String[] roles;

		private Date issuedAt;

		private Date expiresAt;

		private Claims(DecodedJWT decodedJWT) {
			Claim username = decodedJWT.getClaim("username");
			if (!username.isNull()) {
				this.username = username.asString();
			}
			Claim roles = decodedJWT.getClaim("roles");
			if (!roles.isNull()) {
				this.roles = roles.asArray(String.class);
			}
			this.issuedAt = decodedJWT.getIssuedAt();
			this.expiresAt = decodedJWT.getExpiresAt();
		}

		private Claims(String username, String[] roles) {
			this.username = username;
			this.roles = roles;
		}

		public static Claims of(String username, String[] roles) {
			return new Claims(username, roles);
		}
	}
}
