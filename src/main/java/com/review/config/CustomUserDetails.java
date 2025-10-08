package com.review.config;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.review.entity.userEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;



	//로그인한 사용자의 세션정보들을 담아두기위한 class
	//여기서 필요한 세션정보를 꺼내서 씀
	public class CustomUserDetails implements UserDetails ,Serializable , OAuth2User {
		private static final long serialVersionUID = 1L; 

	    // 1. 네 DB 엔티티를 필드로 가집니다.
	    private final userEntity userEntity; 
	    
	    private Map<String, Object> attributes; 
	    
	    // 2. 생성자로 UserEntity를 받습니다.
	    public CustomUserDetails(userEntity userEntity, Map<String, Object> attributes) {
	        this.userEntity = userEntity;
	        this.attributes = attributes;
	    }
	    public CustomUserDetails(userEntity userEntity) {
	    	this.userEntity = userEntity;
	    }
	    
	    public userEntity getUserEntity() {
	    	return userEntity;
	    }

	    // 3. 권한(Role) 목록을 반환합니다. (가장 중요)
	    @Override
	    public Collection <? extends GrantedAuthority> getAuthorities() {
	    	String userRole = userEntity.getRole();
	        return Collections.singletonList(new SimpleGrantedAuthority(userRole));
	    }

	    // 4. 비밀번호 반환 (암호화된 DB 비밀번호)
	    @Override
	    public String getPassword() {
	        return userEntity.getPassword();
	    }

	    // 5. 사용자명(id : email) 반환 (로그인 ID로 사용할 필드, 네 경우엔 EMAIL)
	    @Override
	    public String getUsername() {
	        return userEntity.getEmail(); 
	    }
	    
	    //닉네임
	    public String getNickname() {
	        return userEntity.getNickname(); 
	    }
	    
	    //생일
	    public String getBirthdate() {
	    	return userEntity.getBirthdate(); 
	    }
	  
	    //유저아이디
	    public Long getUserId() {
	    	return userEntity.getUserId(); 
	    }
	    
	    //유저이름
	    public String getPname() {
	    	return userEntity.getPname(); 
	    }
	    
	    //유저 식별 ID
	    @Override
	    public String getName() {
	        // ⭐ 가장 안전하고 권장되는 방식: DB의 고유 ID 반환 ⭐
	        return userEntity.getUserId().toString(); 
	    }
	    
	    @Override
	    public Map<String, Object> getAttributes() {
	        return attributes;
	    }
	    
	    
	    // 6. 계정 상태 관련 설정 (기본적으로 모두 true)
	    @Override public boolean isAccountNonExpired() { return true; }
	    @Override public boolean isAccountNonLocked() { return true; }
	    @Override public boolean isCredentialsNonExpired() { return true; }
	    @Override public boolean isEnabled() { return true; }
	}