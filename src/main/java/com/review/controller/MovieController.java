package com.review.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.review.entity.userReviewEntity;
import com.review.service.UserReviewService;

@Controller
public class MovieController {
	
	@Autowired
	private UserReviewService userReviewService;

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
	
	
	//역대 영화 사이트
	@GetMapping("/TopRate")
	public String MovieList() {
		return "movies/movies_list";
	}
	
	
	//영화리스트
	@GetMapping("/MoviesList")
	public String NowPlaying() {
		return "movies/movies_list";
	}
	
	
	//영화 상세 정보
	@GetMapping("/detail/{movieId}")
	public String getMovieDetail(@PathVariable("movieId") Long id ,Model model){
		System.out.println("넘어온 영화 ID: " + id);
		
		List<userReviewEntity> existingReviews = userReviewService.getReviewsByMovieId(id);
		
		System.out.println(id + "의 대한 영화 리뷰 갯수 : " + existingReviews.size()); 
		model.addAttribute("apiId", id );
		model.addAttribute("reviews", existingReviews );
		return "movies/movies_detail";
	}
	
	
	
}