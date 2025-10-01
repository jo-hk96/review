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
    private final MovieRepository movieRepository;
    
    @Transactional // 데이터 변경 작업(저장)이므로 트랜잭션 관리 어노테이션 사용
    public userReviewEntity saveReview(UserReviewDTO reviewDto) {
    	
    	 // 2. UserEntity 찾기 (유저의 ID를 확보)
        // 닉네임이 유니크하다면 findByNickname()을 사용하거나, 로그인 정보를 통해 User ID를 직접 가져와야 함.
        // 현재는 닉네임으로 찾았다고 가정함.
	    userEntity user = userRepository.findByNickname(reviewDto.getNickname())
	    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 닉네임입니다."));
	    
	    // 3. MovieEntity 찾기
        // Review 엔티티에 저장할 Movie 엔티티가 필요함
        // orElseThrow()를 사용하여 해당 ID의 영화가 없으면 예외 발생
	    movieEntity movie = movieService.saveIfNotExist(reviewDto.getMovieId());

        userReviewEntity newReview = userReviewEntity.builder()
                .comment(reviewDto.getComment()) // DTO에서 받은 리뷰 내용
                .rating(reviewDto.getRating())   // DTO에서 받은 별점
                .userEntity(user)          // 2번에서 찾은 User Entity
                .movieEntity(movie)        // 3번에서 찾은 Movie Entity
                // reviewId는 DB가 자동으로 생성함 (JPA @GeneratedValue)
                .build();
        

        // 5. Repository를 통해 데이터베이스에 최종 저장
        return userReviewRepository.save(newReview);
    }
    
    //영화 리뷰 가져오기
    public List<userReviewEntity> getReviewsByMovieId(Long apiId) {
    	
        // ⭐ 영화 정보가 DB에 존재함을 보장하는 로직이 필요 없음. 그냥 조회만 하면 됨.
        movieEntity movie = movieRepository.findByApiId(apiId)
                .orElse(null); // 리뷰가 없는 영화일 수도 있으니 null 또는 빈 목록 반환
        
        // 2. 만약 해당 영화 정보 자체가 DB에 없다면, 리뷰도 당연히 없음.
        if (movie == null) {
            return Collections.emptyList(); 
        }
        // 3. MovieEntity를 기준으로 모든 리뷰를 조회하여 반환
        // JPA가 UserEntity와 MovieEntity를 JOIN해서 가져옴
        return userReviewRepository.findByMovieEntityOrderByRegDateDesc(movie);
    }
    
    public List<UserReviewDTO> getRecentReviews(){
    	
    	List<userReviewEntity> recentReview = userReviewRepository.findTop5ByOrderByRegDateDesc();
    	
    	return recentReview.stream()
    			.map(UserReviewDTO::fromEntity)
    			.collect(Collectors.toList());
    }
    
	
}
	    
	
