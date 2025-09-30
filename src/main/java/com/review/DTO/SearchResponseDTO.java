package com.review.DTO;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class SearchResponseDTO {
	// TMDB의 루트 필드
    @JsonProperty("page")
    private int page;
    
    @JsonProperty("total_pages")
    private int totalPages;
    
    @JsonProperty("total_results")
    private int totalResults;

    // ⭐️⭐️ 검색된 개별 영화 목록을 담는 리스트 (핵심!) ⭐️⭐️
    // TMDB의 필드명은 'results' (복수)입니다.
    @JsonProperty("results") 
    private List<movieDTO> results; 
	
}
