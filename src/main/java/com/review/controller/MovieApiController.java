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
	    
	    
	    private List<movieDTO> applyUserRatings(List<movieDTO> tmdbMovies) {
	        // 널 체크: 리스트가 null이면 빈 리스트를 반환하여 오류를 방지합니다.
	        if (tmdbMovies == null) {
	            return Collections.emptyList(); // java.util.Collections 임포트 필요
	        }
	        
	        // TMDB에서 받은 영화 목록을 하나씩 순회합니다.
	        for (movieDTO movie : tmdbMovies) {
	            // 1. 해당 영화의 TMDB ID (우리 DB에서는 apiId)를 가져옵니다.
	            Long apiId = movie.getApiId(); 
	            
	            // 2. ⭐ UserReviewService를 호출하여 우리 DB의 평균 평점을 계산합니다. ⭐
	            double userAvgRating = userReviewService.getAverageRatingByApiId(apiId); 
	            
	            // 3. MovieDTO의 필드에 계산된 평점을 설정합니다. 
	            //    (movieDTO에 setOurAverageRating()이 있어야 함)
	            movie.setOurAverageRating(userAvgRating);
	            
	            // 디버깅: 평점이 잘 합쳐졌는지 서버 콘솔로 확인하고 싶다면 아래 주석을 풀어봐.
	            // System.out.println("통합 평점 - ID: " + apiId + ", Rating: " + userAvgRating);
	        }
	        
	        // 평점이 합쳐진 최종 리스트를 반환합니다.
	        return tmdbMovies;
	    }
	    
	    @GetMapping("/api/detail/{apiId}")
	    public ResponseEntity<Map<String, Object>> getMovieDetailData(@PathVariable("apiId") Long id) { // userDetails도 제거

	        // 1. 필요한 평균 평점 데이터만 가져옵니다.
	        double averageRating = userReviewService.getAverageRatingByApiId(id);
	        
	        System.out.println(id + "에 대한 서버 평균 별점: " + averageRating); // 로그 확인용

	        // 2. 데이터를 Map에 담아 JSON으로 반환
	        Map<String, Object> data = new HashMap<>();
	        data.put("apiId", id);
	        data.put("averageRating", averageRating); 
	        
	        return ResponseEntity.ok(data); // JSON 응답: {"apiId": 12345, "averageRating": 4.2}
	    }
}