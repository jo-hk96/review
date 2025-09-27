package com.review.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MovieController {

	//메인홈
	@GetMapping("/")
	public String home() {
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
	public String getMovieDetail(@PathVariable("movieId") Long id){
		System.out.println("넘어온 영화 ID: " + id);
		return "movies/movies_detail";
	}
	
	
	
	
}