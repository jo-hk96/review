package com.review.service;


import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.review.DTO.OAuth2Attributes;
import com.review.config.CustomUserDetails;
import com.review.entity.userEntity;
import com.review.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
	
	private final UserRepository userRepository;
	
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
			// 구글 서버에서 사용자 정보를 가져옴
			OAuth2User oauth2User =  super.loadUser(userRequest);
			// 2. birthdayData 자체를 출력하여 내부 구조를 확인합니다.
			
			//현재 로그인을 진행 중인 서비스를 구분
			String registrationId = userRequest.getClientRegistration().getRegistrationId();
			
			// 3. OAuth2Attributes DTO를 사용해 사용자 정보를 통일된 형식으로 변환합니다.
			OAuth2Attributes attributes = OAuth2Attributes.of(registrationId, oauth2User.getAttributes());

	        // 4. DB에 사용자가 이미 있는지 확인하고, 있으면 업데이트, 없으면 저장합니다.
	        userEntity user = saveOrUpdate(attributes);

	        // 5. 시큐리티 세션에 등록될 사용자 객체를 생성하여 반환합니다.
	        return new CustomUserDetails(
	                user,                           // 첫 번째 인자: DB에서 가져온 UserEntity
	                oauth2User.getAttributes()      // 두 번째 인자: 구글에서 받은 원본 Map<String, Object>
	                );
	    }
	        
		    // DB에 저장 또는 업데이트하는 핵심 로직
		    private userEntity saveOrUpdate(OAuth2Attributes attributes) {
		        // 소셜 타입과 이메일로 기존 사용자를 찾습니다.
		        userEntity user = userRepository.findByEmail(attributes.getEmail())
		                // DB에 없으면 새로 만들고, 있으면 기존 정보를 가져옵니다.
		                .map(entity -> entity.update(attributes.getName())) // 이름 업데이트만 한다고 가정
		                .orElse(attributes.toEntity()); // 새 UserEntity 객체 생성
		        
		        // DB에 저장 (JPA가 ID를 보고 insert/update를 결정)
		        return userRepository.save(user);
	}
}
