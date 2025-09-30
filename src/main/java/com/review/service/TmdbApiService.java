package com.review.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.review.DTO.SearchResponseDTO;
import com.review.DTO.movieDTO;

@Service
public class TmdbApiService {

	
	private final WebClient webClient;
	private final String bearerToken;
	
	
	//properties 에 있는 tmdb.api.base-url :api url , tmdb.api.bearer-token : bearer token
	public TmdbApiService(WebClient.Builder webClientBuilder,
						@Value("${tmdb.api.base-url}") String baseUrl,
						@Value("${tmdb.api.bearer-token}") String bearerToken) {
		
		
		this.webClient = webClientBuilder.baseUrl(baseUrl).build();
		this.bearerToken = bearerToken;
	}
	
	
	
	
	public movieDTO getMovieDetail(Long apiId) {
		String trimmedToken = bearerToken.trim();
		try {
			return webClient.get()
					.uri("/movie/{apiId}?language=ko-KR" , apiId)
					.header("Authorization", "Bearer " + trimmedToken)
					.retrieve() //응답을 처리합니다.
					.bodyToMono(movieDTO.class)// 응답 본문을 DTO로 변환
					.block(); //비동기 호출을 동기식으로 블로킹 하여 결과를 기다림
			
		}catch(WebClientResponseException e) {
			//API에서 에러 응답
			System.err.println("TMDB API 오류 발생: " + e.getStatusCode() + " - " + e.getMessage());
            // 해당 영화가 존재하지 않거나(404) 다른 문제 발생 시 null 반환 또는 사용자 정의 예외 던지기
            return null; 
		}catch(Exception e) {
			System.err.println("API 호출 중 오류 발생:" + e.getMessage());
			return null;
		}
		
	}
	
	
	//API에서 영화 제목으로 검색하는 함수
	//movieDTO에 담긴 영화 정보들을 불러옴
	public List<movieDTO> searchMovies(String query) {
	    // 1. 검색어가 없으면 빈 목록 반환 (API 호출 필요 없음)
	    if (query == null || query.trim().isEmpty()) {
	        return Collections.emptyList();
	    }
	    
	    // 2. 토큰 앞뒤 공백 제거
	    String trimmedToken = bearerToken.trim();
	    
	    try {
	        // ⭐️⭐️ WebClient 호출: UriBuilder를 사용하여 한글 인코딩 문제를 해결합니다. ⭐️⭐️
	        SearchResponseDTO responseDto = webClient.get()
	            // .uri() 대신 .uriBuilder()를 사용합니다.
	            .uri(uriBuilder -> uriBuilder
	                .path("/search/movie")              // API 경로
	                .queryParam("query", query)         // ⭐️ 한글 검색어 안전하게 인코딩 ⭐️
	                .queryParam("language", "ko-KR")    // 언어 설정
	                .build())
	            .header("Authorization", "Bearer " + trimmedToken)
	            .retrieve()
	            .bodyToMono(SearchResponseDTO.class) // ⭐️ 전체 응답을 SearchResponseDTO로 받습니다. ⭐️
	            .block();

	        // 3. 결과 추출 후 반환
	        if (responseDto != null && responseDto.getResults() != null) {
	            return responseDto.getResults(); // MovieDTO 목록 반환
	        }
	        return Collections.emptyList();
	        
	    } catch (WebClientResponseException e) {
	        // HTTP 4xx, 5xx 에러 처리 (TMDB API 검색 오류)
	        System.out.println("TMDB API 검색 오류: " + e.getStatusCode() + " - " + e.getMessage());
	        return Collections.emptyList();
	    } catch (Exception e) {
	        // 기타 연결 오류 처리
	        System.err.println("API 호출 중 오류 발생: " + e.getMessage());
	        return Collections.emptyList();
	    }
	}
}	
