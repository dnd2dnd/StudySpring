package com.server.study.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * description    :
 * packageName    : com.server.study.domain.user
 * fileName       : Role
 * author         : tkfdk
 * date           : 2023-06-10
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-10        tkfdk       최초 생성
 */
@Getter
@RequiredArgsConstructor
public enum Role {
	ADMIN("ROLE_ADMIN"), USER("ROLE_USER");

	private final String role;
}
