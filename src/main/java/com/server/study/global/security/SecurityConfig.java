package com.server.study.global.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.server.study.global.security.jwt.CustomJwtFilter;
import com.server.study.global.security.oauth2.CustomAuthorizationRequestRepository;
import com.server.study.global.security.oauth2.handler.CustomOAuth2AuthenticationFailureHandler;
import com.server.study.global.security.oauth2.handler.CustomOAuth2AuthenticationSuccessHandler;
import com.server.study.global.security.oauth2.service.CustomOAuth2UserService;
import com.server.study.global.security.oauth2.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * description    :
 * packageName    : com.server.study.global.security
 * fileName       : SecurityConfig
 * author         : tkfdk
 * date           : 2023-06-20
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-20        tkfdk       최초 생성
 */
@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final CustomJwtFilter customJwtFilter;
	private final CustomAuthorizationRequestRepository AuthorizationRequestRepository;
	private final CustomUserDetailsService UserDetailsService;
	private final CustomOAuth2UserService oAuth2UserService;
	private final CustomOAuth2AuthenticationSuccessHandler oAuth2AuthorizationSuccessHandler;
	private final CustomOAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.headers().frameOptions().sameOrigin()
			.and()
			.httpBasic().disable()
			.formLogin().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

			// 401, 403 Exception 핸들링
			.and()
			.exceptionHandling()
			.authenticationEntryPoint(jwtAuthenticationEntryPoint)
			.accessDeniedHandler(jwtAccessDeniedHandler)

			// HttpServletRequest를 사용하는 요청들에 대한 접근 제한 설정
			.and()
			.authorizeRequests()
			.anyRequest().authenticated()

			.and()
			.oauth2Login()
			.authorizationEndpoint().baseUri("/oauth2/authorize")
			.authorizationRequestRepository(AuthorizationRequestRepository)
			.and()
			.redirectionEndpoint().baseUri("/oauth2/callback/**")
			.and()
			.userInfoEndpoint().userService(oAuth2UserService)
			.and()
			.successHandler(oAuth2AuthorizationSuccessHandler)
			.failureHandler(oAuth2AuthenticationFailureHandler);
		http.addFilterBefore(customJwtFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

}
