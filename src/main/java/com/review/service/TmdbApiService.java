package com.review.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

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
	public List<movieDTO> searchMovies(String query){
		
		//API의 검색 엔드포인트 경로
		String path = "/search/movie?query={query}&language=ko-KR";
		//베럴토큰 앞뒤 공백 제거
		String trimmedToken = bearerToken.trim();
		
		
			//응답을 movieDTO.class 로 받음
		try {
			movieDTO responseDto = webClient.get()
					.uri(path,query)
					.header("Authorization", "Bearer " + trimmedToken)
					.retrieve()
					.bodyToMono(movieDTO.class) 
					.block();
			//movieDTO 인스턴스에 'movieResults' 필드에 담긴 영화목록만 추출
			if(responseDto != null && responseDto.getResults() != null) {
				return responseDto.getResults();
				
			}
	
			return Collections.emptyList();
		}catch(WebClientResponseException e) {
			System.out.println("TMDB API 검색 오류:" + e.getStatusCode() + " - " + e.getMessage());
			return Collections.emptyList();
		}catch (Exception e) {
			System.err.println("API 호출 중 오류 발생:" + e.getMessage());
			return Collections.emptyList();
		}
	
	}
}	
