package com.review.DTO;

import java.time.LocalDateTime;


import com.review.entity.userEntity;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class UserDTO {
		private Long userId; //유저 고유 ID
	    private String email; //로그인 할 Email
	    private String password; //비번
	    private String nickname; //닉네임
		private String birthdate; // 생일
		private String pname; // 이름
		private LocalDateTime createdAt; // 가입 날짜 및 시간
		private String role; // 관리자 권한 //예: "ROLE_USER", "ROLE_ADMIN" 등의 문자열 저장 
		
	    
	    public userEntity toEntity() {
	        return userEntity.builder()
	        	.email(this.email) // userEntity의 email 필드로 매핑
                .password(this.password)
                .nickname(this.nickname)
                .birthdate(this.birthdate)
                .pname(this.pname)
                .role(this.role)
                // ID는 DB가 시퀀스로 자동 생성하므로 여기선 제외
                .build();
	    }
	    
}
