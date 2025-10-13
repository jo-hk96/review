package com.review.controller;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.review.DTO.TmdbResponseDTO;
import com.review.DTO.movieDTO;
import com.review.config.CustomUserDetails;
import com.review.service.MovieLikeService;
import com.review.service.TmdbApiService;
import com.review.service.UserReviewService;

import lombok.RequiredArgsConstructor;



@RestController
@RequiredArgsConstructor
public class MovieApiController {

		private final MovieLikeService movieLikeService;
		private final TmdbApiService tmdbApiService;
		private final UserReviewService userReviewService;
		
		
		//영화정보 좋아요 비동기API
		@PostMapping("/api/MovieLike/{apiId}")
		public ResponseEntity<Boolean> toggleLike(@PathVariable Long apiId,
				@AuthenticationPrincipal CustomUserDetails userDetails){
			Long cuUserId = userDetails.getUserId();
			boolean isLiked = movieLikeService.toggleLike(cuUserId,apiId);
			return ResponseEntity.ok(isLiked);
		}
	

	    // 영화 비동기 검색
	    @GetMapping("/api/movies/search") 
	    public List<movieDTO> searchMovieByApi(@RequestParam("query") String query) {
	        List<movieDTO> searchResults = tmdbApiService.searchMovies(query); 
	        //공통메서드호출
	        return applyUserRatings(searchResults); 
	    }
	    
	    
	    @GetMapping("/api/movies/list")
	    public TmdbResponseDTO getMoviesListApi(
	            @RequestParam("category") String category, 
	            @RequestParam("page") int page) {
	        
	        // 1. TMDB에서 특정 카테고리와 페이지의 목록을 가져옵니다.
	        // (TmdbResponseDTO를 반환하고, 내부에는 total_pages와 List<movieDTO> results가 있음)
	        TmdbResponseDTO tmdbResponse = tmdbApiService.getMoviesByCategory(category, page); 
	        // 2. 평점을 통합합니다. (results 리스트만 추출)
	        List<movieDTO> tmdbMovies = tmdbResponse.getResults(); 
	        List<movieDTO> moviesWithRatings = applyUserRatings(tmdbMovies); 
	        // 3. 통합된 리스트로 응답 DTO의 결과를 덮어씌웁니다.
	        tmdbResponse.setResults(moviesWithRatings);
	        // 4. total_pages와 평점 통합 리스트를 포함한 JSON을 클라이언트에게 반환합니다.
	        return tmdbResponse; 
	    }
	    
	    
	    
	    //리뷰 평점 평균 계산
	    private List<movieDTO> applyUserRatings(List<movieDTO> tmdbMovies) {
	        if (tmdbMovies == null) {
	            return Collections.emptyList();
	        }
	        // TMDB에서 받은 영화 목록을 하나씩 돔
	        for (movieDTO movie : tmdbMovies) {
	            // Tmdb의 영화고유 Id를 가져옴
	            Long apiId = movie.getApiId(); 
	            double userAvgRating = userReviewService.getAverageRatingByApiId(apiId); 
	            movie.setOurAverageRating(userAvgRating);
	        }
	        return tmdbMovies;
	    }
	    
	    
	    
	    @GetMapping("/api/detail/{apiId}")
	    public ResponseEntity<Map<String, Object>> getMovieDetailData(@PathVariable("apiId") Long id) { // userDetails도 제거
	        // 1. 필요한 평균 평점 데이터만 가져옵니다.
	        double averageRating = userReviewService.getAverageRatingByApiId(id);
	        // 2. 데이터를 Map에 담아 JSON으로 반환
	        Map<String, Object> data = new HashMap<>();
	        data.put("apiId", id);
	        data.put("averageRating", averageRating); 
	        return ResponseEntity.ok(data); // JSON 응답: {"apiId": 12345, "averageRating": 4.2}
	    }
}