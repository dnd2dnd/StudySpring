package com.server.study.global.security.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.server.study.global.security.RedisService;
import com.server.study.global.security.dto.AuthDto;
import com.server.study.global.security.oauth2.exception.OAuth2AuthenticationProcessingException;
import com.server.study.global.security.oauth2.service.CustomUserDetailsService;
import com.server.study.global.security.oauth2.userinfo.CustomUserPrincipal;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;

/**
 * description    :
 * packageName    : com.server.study.global.security.jwt
 * fileName       : TokenProvider
 * author         : tkfdk
 * date           : 2023-06-16
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-16        tkfdk       최초 생성
 */
@Service
@RequiredArgsConstructor
public class TokenProvider {
	@Value("${app.auth.tokenSecret}")
	private String tokenSecret;
	@Value("${app.auth.tokenExpirationMsec}")
	private long tokenExpirationMsec;

	private final RedisService redisService;
	private final CustomUserDetailsService customUserDetailsService;

	public AuthDto.TokenDto createTokenOAuth2(Authentication authentication) {
		CustomUserPrincipal userPrincipal = (CustomUserPrincipal)authentication.getPrincipal();
		return createToken(userPrincipal.getId(), userPrincipal.getEmail(), getAuthorities(authentication));
	}

	public AuthDto.TokenDto createToken(UUID userId, String email, String authorities) {
		String accessToken = Jwts.builder()
			.setHeader(createHeader())
			.claim("email", email)
			.claim("authorities", authorities)
			.setSubject("accessToken")
			.setExpiration(createExpiredDate(1))
			.signWith(SignatureAlgorithm.HS512, tokenSecret)
			.compact();

		String refreshToken = Jwts.builder()
			.setHeader(createHeader())
			.setSubject("refreshToken")
			.setExpiration(createExpiredDate(2 * 7 * 24))
			.signWith(SignatureAlgorithm.HS512, tokenSecret)
			.compact();

		redisService.setValuesWithTimeout(userId.toString(), refreshToken,
			getClaims(refreshToken).getExpiration().getTime());

		return new AuthDto.TokenDto(accessToken, refreshToken);
	}

	public Claims getClaims(String token) {
		return Jwts.parser()
			.setSigningKey(tokenSecret)
			.parseClaimsJws(token)
			.getBody();
	}

	public String getAuthorities(Authentication authentication) {
		return authentication.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token);
			return true;
		} catch (SignatureException e) { // 유효하지 않은 JWT 서명
			throw new OAuth2AuthenticationProcessingException("not valid jwt signature");
		} catch (MalformedJwtException e) { // 유효하지 않은 JWT
			throw new OAuth2AuthenticationProcessingException("not valid jwt");
		} catch (io.jsonwebtoken.ExpiredJwtException e) { // 만료된 JWT
			throw new OAuth2AuthenticationProcessingException("expired jwt");
		} catch (io.jsonwebtoken.UnsupportedJwtException e) { // 지원하지 않는 JWT
			throw new OAuth2AuthenticationProcessingException("unsupported jwt");
		} catch (IllegalArgumentException e) { // 빈값
			throw new OAuth2AuthenticationProcessingException("empty jwt");
		}
	}

	private Map<String, Object> createHeader() {
		Map<String, Object> header = new HashMap<>();
		header.put("typ", "JWT");
		header.put("alg", "HS256");
		header.put("regDate", System.currentTimeMillis());
		return header;
	}

	private Date createExpiredDate(int time) {
		Date now = new Date();
		return new Date(now.getTime() + tokenExpirationMsec * time);
	}

	public Authentication getAuthentication(String token) {
		String email = getClaims(token).get("email").toString();
		CustomUserPrincipal userDetails = customUserDetailsService.loadUserByUsername(email);
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}
}
