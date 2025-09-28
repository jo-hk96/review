package com.review.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.review.DTO.UserReviewDTO;
import com.review.entity.userReviewEntity;
import com.review.service.UserReviewService;

@RestController
public class UserReviewApiController {
	
	
	@Autowired
	private UserReviewService userReviewService;
	
	
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
