package com.review.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.review.config.CustomUserDetails;
import com.review.entity.userReviewEntity;
import com.review.service.MovieLikeService;
import com.review.service.UserReviewService;

@Controller
public class MovieController {
	
	@Autowired
	private UserReviewService userReviewService;

	@Autowired
	private MovieLikeService movieLikeService;
	
	//@Autowired
	//private final TmdbApiService tmdbApiService;
	
	//메인홈
	@GetMapping("/")
	public String Home(Principal principal, Model model) {
		
		 if (principal != null) {
	            // principal.getName()은 UserDetailsService에서 반환한 getUsername() 값,
	            //로그인한 사용자의 이메일이 됩니다.
	            String loggedInUserEmail = principal.getName(); 
	            
	            // 이메일에서 '@' 앞의 아이디 부분만 표시하고 싶다면:
	            // String username = loggedInUserEmail.split("@")[0];
	            
	            model.addAttribute("username", loggedInUserEmail);
	        }
		return "index/index";
	}
	
	//영화리스트
			@GetMapping("/MoviesList")
			public String handleMovieListing(@RequestParam(value = "movieSearch",required = false) String query, Model model){
				
				//검색어 query를 담아서 list페이지에 넘김
				model.addAttribute("searchQuery" ,query);
				
				return "movies/movies_list";
			}
			
	
	//영화 상세 정보
	@GetMapping("/detail/{movieId}")
	public String getMovieDetail(@PathVariable("movieId") Long id , 
						@AuthenticationPrincipal CustomUserDetails userDetails ,Model model){
		
		System.out.println("넘어온 영화 ID: " + id);
		
		//리뷰 목록 가져오기
		List<userReviewEntity> existingReviews = userReviewService.getReviewsByMovieId(id);
		
		//좋아요 상태 조회 로직 추가
		boolean isLiked = false;
		
		
		//로그인 했을 때만 좋아요 상태를 조회합니다.
		if(userDetails != null) {
			Long userId = userDetails.getUserId();
			
			 // MovieLikeService의 getLikeStatus 메서드를 사용해 DB 조회
	        isLiked = movieLikeService.getLikeStatus(userId, id); 
		}
		System.out.println(id + "의 대한 영화 리뷰 갯수 : " + existingReviews.size()); 
		model.addAttribute("apiId", id );
		model.addAttribute("isLiked", isLiked);
		model.addAttribute("reviews", existingReviews );
		return "movies/movies_detail";
	}
	
	
	
}