package com.review.DTO;

import java.time.LocalDate;

import lombok.Data;


@Data
public class movieDTO {
		private Long movieId; // 영화 고유 ID
	    private String originalTitle; //원제
	    private String title; //제목
	    private String overview; //줄거리
	    private String posterPath; //포스터
	    private LocalDate releaseDate; // 개봉날짜
	
}
