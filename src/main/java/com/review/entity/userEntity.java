package com.review.entity;


import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "MOVIE_USER")
@Data
@Builder
public class userEntity {
	
		@SequenceGenerator(
		    name = "USER_SEQ_GENERATOR", 
		    sequenceName = "USER_SEQ",
		    allocationSize = 1
		)
		@GeneratedValue(
			    strategy = GenerationType.SEQUENCE, 
			    generator = "USER_SEQ_GENERATOR"
			)
	   	@Id // 이 필드를 기본 키(PK)로 지정
	    @Column(name = "USER_ID")
	   	private Long userId; //유저 고유 ID
		
		@Column(name = "EMAIL" , nullable = false) 
	    private String email; //로그인 할 Email
		
		@Column(name = "PASSWORD" , nullable = false) 
	    private String password; //비번
		
		@Column(name = "NICKNAME" , nullable = false)
	    private String nickname; //닉네임
		
		@Column(name = "birthdate" , nullable = false)
		private String birthdate; // 생일
		
		//DB에 최초 저장(INSERT)될 때 현재 시간을 자동으로 기록
		@CreationTimestamp
		@Column(name = "CREATED_AT", nullable = false, updatable = false) // ⭐️ updatable = false 설정 ⭐️
		private LocalDateTime createdAt; // 가입 날짜 및 시간
	
		
}
