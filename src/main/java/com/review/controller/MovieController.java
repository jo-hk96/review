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
		return "movies/top_rate";
	}
	
	
	//현재 개봉중인 영화
	@GetMapping("/NowPlaying")
	public String NowPlaying() {
		return "movies/now_playing";
	}
	
	//영화 상세 정보
	@GetMapping("/detail/{movieId}")
	public String getMovieDetail(@PathVariable("movieId") Long id){
		System.out.println("넘어온 영화 ID: " + id);
		return "movies/movies_detail";
	}
	
}