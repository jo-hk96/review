package com.review.DTO;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class movieDTO {
		private Long movieId; // 영화 고유 ID
		
		private Long apiId;
		
		
		@JsonProperty("original_title")
	    private String originalTitle; //원제
	    private String title; //제목
	    private String overview; //줄거리
	    
	    @JsonProperty("poster_path")
	    private String posterPath; //포스터
	    
	    
	    //DB의 MOVIE_RELEASEDATE
	    @JsonProperty("release_date")
	    @JsonFormat(pattern = "yyyy-MM-dd")
	    private LocalDate releaseDate; // 개봉날짜
	    
	
}
