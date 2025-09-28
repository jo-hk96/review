package com.review.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.review.entity.userEntity;


//사용자 정보 레포지토리
@Repository
public interface UserRepository extends JpaRepository<userEntity, Long> {

	Optional<userEntity> findByEmail(String email); // 이메일
	Optional<userEntity> findByNickname(String nickname); // 닉네임
}
