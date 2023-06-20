package com.server.study.global.security.oauth2.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.server.study.domain.user.User;
import com.server.study.domain.user.repository.UserRepository;
import com.server.study.global.security.oauth2.userinfo.CustomUserPrincipal;

import lombok.RequiredArgsConstructor;

/**
 * description    :
 * packageName    : com.server.study.global.security.oauth2.service
 * fileName       : CustomUserDetailsService
 * author         : tkfdk
 * date           : 2023-06-20
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-20        tkfdk       최초 생성
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	private final UserRepository userRepository;

	@Override
	public CustomUserPrincipal loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + email));
		return CustomUserPrincipal.create(user);
	}
}
