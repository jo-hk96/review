package com.review.service;


import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.review.DTO.UserReviewDTO;
import com.review.DTO.movieDTO;
import com.review.entity.movieEntity;
import com.review.entity.userEntity;
import com.review.entity.userReviewEntity;
import com.review.repository.MovieRepository;
import com.review.repository.UserRepository;
import com.review.repository.UserReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor //final 필드들을 주입(DI)하기 위한 Lombok 어노테이션
public class UserReviewService{
	private final UserReviewRepository userReviewRepository;
    private final UserRepository userRepository; 
    private final MovieService movieService;
    private final TmdbApiService tmdbApiService;
    
    @Transactional // 데이터 변경 작업(저장)이므로 트랜잭션 관리 어노테이션 사용
    public userReviewEntity saveReview(UserReviewDTO userReviewDTO , Long apiId ) {
    	
    	 // 2. UserEntity 찾기 (유저의 ID를 확보)
        // 닉네임이 유니크하다면 findByNickname()을 사용하거나, 로그인 정보를 통해 User ID를 직접 가져와야 함.
	    userEntity user = userRepository.findByNickname(userReviewDTO.getNickname())
	    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 닉네임입니다."));
	    
	    //api_id로 영화 제목 조회
	    String movieTitle = tmdbApiService.getMovieTitle(userReviewDTO.getApiId());
	    
	    
	    //userReviewEntity 에서 리뷰 정보를 저장
        userReviewEntity newReview = userReviewEntity.builder()
        		.userEntity(user)          // 2번에서 찾은 User Entity
                .comment(userReviewDTO.getComment()) // DTO에서 받은 리뷰 내용
                .rating(userReviewDTO.getRating())   // DTO에서 받은 별점
                .apiId(userReviewDTO.getApiId())
                .title(movieTitle)
                .build();
        

        //Repository를 통해 데이터베이스에 최종 저장
        return userReviewRepository.save(newReview);
    }
    //영화 리뷰 가져오기
    public List<userReviewEntity> getReviewsByMovieId(Long apiId) {
        //apiId를 조회해서 반환
        return userReviewRepository.findReviewsByApiIdNative(apiId);
    }
    
    //메인에 사용자 리뷰목록을 최신순으로 5개만 보여줌
    public List<UserReviewDTO> getRecentReviews(){
    	List<userReviewEntity> recentReview = userReviewRepository.findTop5ByOrderByRegDateDesc();
    	return recentReview.stream()
    			.map(UserReviewDTO::fromEntity)
    			.collect(Collectors.toList());
    }
}
	    
	
