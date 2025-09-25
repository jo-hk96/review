package com.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.review.entity.userEntity;

@Repository
public interface UserRepository extends JpaRepository<userEntity, Long> {

    //이메일 중복 확인을 위한 메서드 (Spring Data JPA가 자동으로 SQL을 생성함)
    boolean existsByEmail(String email);
	
}
