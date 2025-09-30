package com.review.DTO;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class movieDTO {
	private Long movieId; // 영화 고유 ID
		
		//검색된 개별 영화 목록을 담는 리스트
		@JsonProperty("result")
		private List<movieDTO> results;
		
		//@JsonProperty("id") api에서 보낸 필드 이름과 매칭
		//TMDB의 고유 ID
		@JsonProperty("id")
		private Long apiId;
		
		//원제
		@JsonProperty("original_title")
	    private String originalTitle; 
		
		//제목
		@JsonProperty("title")
	    private String title; 
	    
		//줄거리
		@JsonProperty("overview")
	    private String overview; 
	    
		//영화 좋아요 평점
		@JsonProperty("vote_average")
		private Double voteAverage;
		
		//포스터
	    @JsonProperty("poster_path")
	    private String posterPath; 
	    
	    //개봉날짜
	    //DB의 MOVIE_RELEASEDATE
	    @JsonProperty("release_date")
	    @JsonFormat(pattern = "yyyy-MM-dd")
	    private LocalDate releaseDate; 
	
}
