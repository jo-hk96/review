package com.review.DTO;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TmdbResponseDTO {
		
	
		//영화들의 전체 목록을 담기 위한 DTO
	
	 	@JsonProperty("total_pages") // JSON의 키 이름("total_pages")과 매핑
	    private int total_pages; 
	    
	    @JsonProperty("total_results")
	    private int total_results;
	    
	    @JsonProperty("results")
	    private List<movieDTO> results;
	    
	    
	
}
