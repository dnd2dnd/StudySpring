package com.server.study.global.security.oauth2.userinfo;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.server.study.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * description    :
 * packageName    : com.server.study.global.oauth2.userinfo
 * fileName       : CustomUserDetails
 * author         : tkfdk
 * date           : 2023-06-10
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-10        tkfdk       최초 생성
 */
@Getter
@AllArgsConstructor
public class CustomUserPrincipal implements OAuth2User, UserDetails {
	private UUID id;
	private String email;
	private String password;
	private Collection<? extends GrantedAuthority> authorities;
	@Setter
	private Map<String, Object> attributes;

	public static CustomUserPrincipal create(User user) {
		List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

		return new CustomUserPrincipal(user.getId(), user.getEmail(), user.getPassword(), authorities, null);
	}

	public static CustomUserPrincipal create(User user, Map<String, Object> attributes) {
		CustomUserPrincipal customUserPrincipal = CustomUserPrincipal.create(user);
		customUserPrincipal.setAttributes(attributes);
		return customUserPrincipal;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getName() {
		return id.toString();
	}
}
