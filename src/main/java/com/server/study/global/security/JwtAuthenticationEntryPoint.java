package com.server.study.global.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * description    :
 * packageName    : com.server.study.global.security
 * fileName       : JwtAuthenticationEntryPoint
 * author         : tkfdk
 * date           : 2023-06-20
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-20        tkfdk       최초 생성
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException, ServletException {

		// response.setCharacterEncoding("utf-8");
		// response.sendError(401, "잘못된 접근입니다.");
		log.error("가입되지 않은 사용자 접근");
		// 유효한 자격증명을 제공하지 않고 접근하려 할때 401
		final Map<String, Object> body = new HashMap<>();
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		// 응답 객체 초기화
		body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
		body.put("error", "Unauthorized");
		body.put("message", authException.getMessage());
		body.put("path", request.getServletPath());
		final ObjectMapper mapper = new ObjectMapper();
		// response 객체에 응답 객체를 넣어줌
		mapper.writeValue(response.getOutputStream(), body);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	}
}
