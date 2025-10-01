package com.review.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.review.DTO.UserReviewDTO;
import com.review.DTO.movieDTO;
import com.review.config.CustomUserDetails;
import com.review.entity.userReviewEntity;
import com.review.service.MovieService;
import com.review.service.UserReviewService;

@RestController
public class UserReviewApiController {
	
	
	@Autowired
	private UserReviewService userReviewService;
	
	@Autowired
	private MovieService movieService;
	
	//사용자 내정보 좋아요 목록
	@GetMapping("/api/user/likedMovies")
	public List<movieDTO> getLikedMovies(@AuthenticationPrincipal CustomUserDetails cud){
		
		//로그인된 사용자 정보를 기반으로 좋아요 목록을 DB/Service에서 가져옴
		Long userId = ((CustomUserDetails) cud).getUserId();
		return movieService.getLikeMoviesByUserId(userId);
	}
	
	
	//유저들의 영화 리뷰 목록
	@PostMapping("/api/userReview")
	public ResponseEntity<?> createReview (@RequestBody UserReviewDTO reviewDto){
		
		 // 1. 서비스 호출 및 DB 저장
	    userReviewEntity newReview = userReviewService.saveReview(reviewDto);
	    
	    // 2. 응답 DTO로 변환 (필수: 닉네임, 내용, 작성일 포함)
	    UserReviewDTO responseDto = UserReviewDTO.fromEntity(newReview); 
	    
	    // ⭐ 3. 저장된 데이터를 HTTP 201 Created와 함께 반환 ⭐
	    return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
	}
	
	
}
