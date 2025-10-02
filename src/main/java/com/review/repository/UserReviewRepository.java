package com.review.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.review.entity.movieEntity;
import com.review.entity.userReviewEntity;

@Repository
public interface UserReviewRepository extends JpaRepository<userReviewEntity, Long> {
	
	
	@Query(value = "SELECT * FROM USER_REVIEW r WHERE r.API_ID = :apiId ORDER BY r.REGDATE DESC", 
	           nativeQuery = true)
	    List<userReviewEntity> findReviewsByApiIdNative(@Param("apiId") Long apiId);
	
	
	    List<userReviewEntity> findTop5ByOrderByRegDateDesc();
}
