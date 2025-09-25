package com.review.DTO;

import com.review.entity.userEntity;

import lombok.Data;

@Data
public class UserDTO {
		private Long userId; //유저 고유 ID
	    private String email; //로그인 할 Email
	    private String password; //비번
	    private String nickname; //닉네임
	    
	    public userEntity toEntity() {
	        // 비밀번호는 실제 서비스에선 반드시 암호화(인코딩)되어야 해!
	        return userEntity.builder()
                .email(this.email) // userEntity의 email 필드로 매핑
                .password(this.password)
                .nickname(this.nickname)
                // ID는 DB가 시퀀스로 자동 생성하므로 여기선 제외
                .build();
	    }
	    
}
