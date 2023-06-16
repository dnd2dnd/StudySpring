package com.server.study;

import java.time.LocalDateTime;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

/**
 * description    :
 * packageName    : com.server.study
 * fileName       : BaseTime
 * author         : tkfdk
 * date           : 2023-06-16
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-16        tkfdk       최초 생성
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTime {

	@CreatedDate
	private LocalDateTime createdDate; // 생성시간

	@LastModifiedDate
	private LocalDateTime updatedDate; // 수정시간
}
