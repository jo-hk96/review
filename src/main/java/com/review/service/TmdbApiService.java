package com.review.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.review.DTO.movieDTO;

@Service
public class TmdbApiService {

	
	private final WebClient webClient;
	private final String bearerToken;
	
	
	public TmdbApiService(WebClient.Builder webClientBuilder,
						@Value("${tmdb.api.base-url}") String baseUrl,
						@Value("${tmdb.api.bearer-token}") String bearerToken) {
		
		
		this.webClient = webClientBuilder.baseUrl(baseUrl).build();
		this.bearerToken = bearerToken;
	}
	
	public movieDTO getMovieDetail(Long apiId) {
		
		try {
			
			return webClient.get()
					.uri("/movie/{apiId}" , apiId)
					.header("Authorization","Bearer" + bearerToken)
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
	
}
