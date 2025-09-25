package com.review.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MovieController {

		
	@GetMapping("/")
	public String home() {
		return "index/index";
	}
	
	@GetMapping("/TopRate")
	public String MovieList() {
		return "movies/top_rate";
	}
	
	@GetMapping("/NowPlaying")
	public String NowPlaying() {
		return "movies/now_playing";
	}
	
}