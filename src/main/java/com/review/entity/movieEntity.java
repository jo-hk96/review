package com.review.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "MOVIE")
@Data
public class movieEntity {
	  
		@Id
		@Column(name = "MOVIE_ID")
	    private Long movieId; // 영화 고유 ID
		
		
		@Column(name = "API_ID", unique = true, nullable = false)
		private Long apiId; 
	    private String originalTitle; //원제
		private String title; //제목
		private String overview; //줄거리
		private String posterPath; //포스터
		private LocalDate releaseDate; // 개봉날짜
}
