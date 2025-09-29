package com.review.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.review.DTO.UserDTO;
import com.review.DTO.UserEditDTO;
import com.review.config.CustomUserDetails;
import com.review.entity.userEntity;
import com.review.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;

    // 사용자 이름(email)으로 사용자 정보를 가져오는 메소드
	@Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        userEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + "을(를) 찾을 수 없습니다."));
        return new CustomUserDetails(user); 
    }
	
	//유저 회원가입
	public void joinUser(UserDTO userDto) {
	    
	    // 1. DTO를 UserEntity로 변환
	    userEntity userEntity = userDto.toEntity(); 
	    
	    // 2. 비밀번호 암호화
	    String encodedPassword = passwordEncoder.encode(userEntity.getPassword());
	    userEntity.setPassword(encodedPassword);
	    
	    
	    //slawk159@naver.com 이라는 이메일에 admin부여
	    if ("1234@1234.com".equals(userEntity.getEmail())) {
	        // 특정 이메일 주소에만 ROLE_ADMIN을 부여
	        userEntity.setRole("ROLE_ADMIN"); //컬럼에 입력 
	        System.out.println(">>> 관리자 계정 생성: " + userEntity.getEmail());
	    } else {
	        // 그 외 모든 계정은 기본 ROLE_USER를 부여
	        userEntity.setRole("ROLE_USER"); 
	    }
	    
	    // 4. DB에 저장
	    userRepository.save(userEntity);
	}
	
	
	//회원수정
	@Transactional
	public void updateUser(Long userId , UserEditDTO userDto) {
		
		userEntity user = userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
		
		//사용자가 입력한 현재비밀번호와 DB에 저장된 비밀번호를 비교
		if(!passwordEncoder.matches(userDto.getCurrentPassword(),user.getPassword())) {
			throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
		}
		
		
		//닉네임이 null이 아니거나 비어있지 않다면 수정
		if(userDto.getNickname() != null && !userDto.getNickname().isEmpty()) {
			user.setNickname(userDto.getNickname());
		}
		
		//새 비밀번호가 null이 아니거나 비어 있지 않다면
		String newPwd = userDto.getNewPassword();
			if(newPwd != null && !newPwd.isEmpty()) {
				
				//새비밀번호 암호화
				String encpwd = passwordEncoder.encode(newPwd);
				
				//암호화한 비번 넣기
				user.setPassword(encpwd);
				
				//@Transactional 자동 save 없으면 적어야댐
				//userRepository.save(user);
			}
		
	}
	
	    
}
	
