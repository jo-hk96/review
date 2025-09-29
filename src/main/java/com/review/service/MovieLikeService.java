package com.review.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.review.entity.MovieLike;
import com.review.entity.MovieLikeId;
import com.review.entity.movieEntity;
import com.review.entity.userEntity;
import com.review.repository.MovieLikeRepository;
import com.review.repository.MovieRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovieLikeService {

	private final MovieRepository movieRepository;
	private final UserService userService;
	//apiID 상세정보를 가져오기위한 service
	private final TmdbApiService tmdbApiService;
	public boolean toggleLike(Long userId , Long apiId) {
		
		//MovieEntity에 정보가 없다면 api 호출후 DB에 저장
		movieEntity movieEntity = movieRepository.findByApiId(apiId).orElseGet(() ->{
			return saveMovieFromApi(apiId);
			
		});
		
		//userEntity 확보
		userEntity userEntity = userService.findById(userId);
		
		//좋아요 복합 키 생성:DB에 저장된 MovieEntity의 PK를 사용
		Long dbMovieId = movieEntity.getMovieId();
		MovieLikeId likeId = new MovieLikeId(userId, dbMovieId);
		
		Optional<MovieLike> existingLike = movieRepository.findById(likeId);
		
		if(existingLike.isPresent()) {
			
			//좋아요 했으면 : 좋아요 기록 DB에서 삭제
			movieRepository.delete(existingLike.get());
			return false; //좋아요 취소됨
		}else {
			//좋아요 하지 않았으면 : MovieLike 엔티티 DB에 저장
			MovieLike newLike = new MovieLike();
			newLike.setId(likeId);
			newLike.setUserEntity(userEntity);
			newLike.setMovieEntity(movieEntity);
			
			movieRepository.save(newLike);//DB에 저장됨
			return true; //좋아요 설정됨
			
		}
		
	}
}
