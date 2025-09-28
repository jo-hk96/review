package com.review.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.review.entity.movieEntity;
import com.review.entity.userReviewEntity;

@Repository
public interface UserReviewRepository extends JpaRepository<userReviewEntity, Long> {
	
	
	//  OrderByRegDateDesc  : JPA에서 함수메서드 이름으로 자동으로 쿼리문을 만들어줌
	//리뷰 댓글 불러오기
	List<userReviewEntity> findByMovieEntityOrderByRegDateDesc(movieEntity movie);
}
