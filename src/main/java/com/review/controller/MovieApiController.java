package com.review.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.review.config.CustomUserDetails;
import com.review.service.MovieLikeService;

import lombok.RequiredArgsConstructor;



@RestController
@RequiredArgsConstructor
public class MovieApiController {

		private final MovieLikeService movieLikeService;
	
		
		
		//영화 좋아요 비동기API
		@PostMapping("/api/MovieLike/{apiId}")
		public ResponseEntity<Boolean> toggleLike(@PathVariable Long apiId,
				@AuthenticationPrincipal CustomUserDetails userDetails){
			
			Long cuUserId = userDetails.getUserId();
			
			boolean isLiked = movieLikeService.toggleLike(cuUserId,apiId);
	
			return ResponseEntity.ok(isLiked);
		}
	
		
		
	
		
		
	
	
	
}