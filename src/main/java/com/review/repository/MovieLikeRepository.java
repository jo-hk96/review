package com.review.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.review.entity.MovieLike;
import com.review.entity.MovieLikeId;

@Repository
public interface MovieLikeRepository extends JpaRepository<MovieLike ,MovieLikeId> {
	//복합 키를 사용하면 Spring Data JPA의 기본 메서드(findById, save ,delete)를 그대로 쓸 수있다
	//특정 영화의 좋아요 개수를 세는 커스텀 쿼리
	long countById_MovieId(Long movieId);
	
	//특정 사용자가 특정 영화를 좋아요 했는지 확인하는 메서드
	boolean existsById_UserIdAndId_MovieId(Long userId , Long movieId);
	
	
}