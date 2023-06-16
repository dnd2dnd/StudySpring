package com.server.study.global.security.oauth2.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * description    :
 * packageName    : com.server.study.global.security.oauth2.exception
 * fileName       : OAuth2AuthenticationProcessingException
 * author         : tkfdk
 * date           : 2023-06-16
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-16        tkfdk       최초 생성
 */
public class OAuth2AuthenticationProcessingException extends AuthenticationException {
	public OAuth2AuthenticationProcessingException(String msg, Throwable t) {
		super(msg, t);
	}

	public OAuth2AuthenticationProcessingException(String msg) {
		super(msg);
	}
}

