package com.review.controller;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.review.DTO.movieDTO;
import com.review.config.CustomUserDetails;
import com.review.service.MovieLikeService;
import com.review.service.TmdbApiService;

import lombok.RequiredArgsConstructor;



@RestController
@RequiredArgsConstructor
public class MovieApiController {

		private final MovieLikeService movieLikeService;
		private final TmdbApiService tmdbApiService;
		
		
		//영화 좋아요 비동기API
		@PostMapping("/api/MovieLike/{apiId}")
		public ResponseEntity<Boolean> toggleLike(@PathVariable Long apiId,
				@AuthenticationPrincipal CustomUserDetails userDetails){
			
			Long cuUserId = userDetails.getUserId();
			
			boolean isLiked = movieLikeService.toggleLike(cuUserId,apiId);
	
			return ResponseEntity.ok(isLiked);
		}
	


	    // ⭐️ 이 경로(fetch 대상)는 반드시 JSON List를 반환해야 합니다. ⭐️
	    @GetMapping("/api/movies/search")
	    public List<movieDTO> searchMovieByApi(@RequestParam("query") String query) {
	        // 검색 서비스 호출: 이 메서드는 List<MovieDTO>를 반환해야 합니다.
	        return tmdbApiService.searchMovies(query); 
	    }
}