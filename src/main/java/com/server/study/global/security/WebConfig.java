package com.server.study.global.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * description    :
 * packageName    : com.server.study.global.security
 * fileName       : WebConfig
 * author         : tkfdk
 * date           : 2023-06-20
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-20        tkfdk       최초 생성
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Value("${app.cors.allowedOrigins}")
	private String allowedOrigins;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry
			.addMapping("/**")
			.allowedOrigins(allowedOrigins) // 외부에서 들어오는 모든 url을 허용
			.allowedMethods("GET", "POST", "PUT", "DELETE") // 허용되는 Method
			.allowedHeaders("*") // 허용되는 헤더
			.allowCredentials(true); // 자격증명 허용
	}
}
