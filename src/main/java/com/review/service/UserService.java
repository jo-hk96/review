package com.review.service;


import org.springframework.stereotype.Service;

import com.review.DTO.UserDTO;
import com.review.entity.userEntity;
import com.review.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	
	
		@Transactional
	    public Long join(UserDTO userDto) {
	        
	        // 1. (필수) 이메일 중복 확인 등의 비즈니스 로직 수행
	        if (userRepository.existsByEmail(userDto.getEmail())) {
	            throw new RuntimeException("이미 존재하는 이메일입니다.");
	        }
	        
	        // 2. DTO를 userEntity로 변환 (비밀번호는 암호화되지 않은 상태)
	        userEntity newUser = userDto.toEntity();
	        
	        // 3. 엔티티를 DB에 저장하고, 저장된 객체를 받음
	        userEntity savedUser = userRepository.save(newUser);
	        
	        // 4. 저장된 엔티티의 ID를 반환 (성공 여부 확인용)
	        return savedUser.getUserId();
	    }
	
	
	    
}
	
