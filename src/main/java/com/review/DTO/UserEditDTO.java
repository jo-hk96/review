package com.review.DTO;

import lombok.Data;

@Data
public class UserEditDTO {

	private String currentPassword;// 현재비밀번호
    private String newPassword; //새 비밀번호
    private String confirmNewPassword; //새 비밀번호 확인
    private String nickname; //닉네임
    
}
