package com.review.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.review.DTO.UserReviewDTO;
import com.review.DTO.movieDTO;
import com.review.config.CustomUserDetails;
import com.review.entity.userReviewEntity;
import com.review.repository.UserReviewRepository;
import com.review.service.MovieService;
import com.review.service.UserReviewService;

@RestController
public class UserReviewApiController {
	
	
	@Autowired
	private UserReviewService userReviewService;
	
	@Autowired
	private MovieService movieService;
	
	@Autowired
	private UserReviewRepository userReviewRepository;
	
	
	
	
	//사용자 내정보 좋아요 목록
	@GetMapping("/api/user/likedMovies")
	public List<movieDTO> getLikedMovies(@AuthenticationPrincipal CustomUserDetails cud){
		//로그인된 사용자 정보를 기반으로 좋아요 목록을 DB/Service에서 가져옴
		Long userId = (cud.getUserId());
		return movieService.getLikeMoviesByUserId(userId);
	}
	
	
	
	//사용자 리뷰 작성 목록
	@GetMapping("/api/user/ReviewMovie")
	public List<UserReviewDTO> getReviewMovies(@AuthenticationPrincipal CustomUserDetails cud){
		//로그인 된 사용자의ID를 추출합니다.
		Long userId = cud.getUserId();
		return userReviewService.getReviewsByUserId(userId);
	}
	
	
	//영화 리뷰 등록
	@PostMapping("/api/userReview")
	public ResponseEntity<?> createReview (@RequestBody UserReviewDTO reviewDto,Long apiId){
		
		 // 1. 서비스 호출 및 DB 저장
	    userReviewEntity newReview = userReviewService.saveReview(reviewDto,apiId);
	    
	    // 2. 응답 DTO로 변환 (필수: 닉네임, 내용, 작성일 포함)
	    UserReviewDTO responseDto = UserReviewDTO.fromEntity(newReview); 
	    
	    // ⭐ 3. 저장된 데이터를 HTTP 201 Created와 함께 반환 ⭐
	    return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
	}
	
	
	
	@GetMapping("/api/reviews")
	public ResponseEntity<List<UserReviewDTO>> getReviewsByMovieId(@RequestParam("apiId") Long apiId) {
	    
	    // 1. 서비스 호출: DB에서 해당 apiId를 가진 모든 리뷰 목록을 가져옴
	    //    (UserReviewService에 이 메소드가 정의되어 있어야 합니다.)
	    List<UserReviewDTO> reviews = userReviewService.getReviewsByMovieApiId(apiId); 
	    
	    //프론트엔드는 이 JSON 배열을 받아 화면에 리뷰 목록을 그립니다.
	    return ResponseEntity.ok(reviews);
	}
	
	

	// ⭐⭐ 1. DELETE API: 리뷰 삭제 ⭐⭐
	// URL: /api/userReview/{reviewId}
	@DeleteMapping("api/userReview/{reviewId}")
	public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId, @AuthenticationPrincipal CustomUserDetails cud) {
	    
	    // 1. 서비스 호출: 리뷰 ID와 현재 로그인한 사용자 ID를 함께 전달하여 권한 확인 및 삭제
	    userReviewService.deleteReview(reviewId, cud.getUserId()); 
	    
	    // 2. HTTP 204 No Content 반환 (삭제 성공 시 보통 응답 본문 없이 상태 코드만 보냄)
	    return ResponseEntity.noContent().build();
	}
	
	

	// ⭐⭐ 2. PUT/PATCH API: 리뷰 수정 ⭐⭐
	// URL: /api/userReview/{reviewId}
	@PatchMapping("api/userReview/{reviewId}")
	public ResponseEntity<UserReviewDTO> updateReview(
	        @PathVariable Long reviewId, 
	        @RequestBody UserReviewDTO updateDto, // 클라이언트가 보낸 원본 DTO
	        @AuthenticationPrincipal CustomUserDetails cud) {
	    
	    // 1. 서비스 호출: 리뷰 ID, 수정 내용, 사용자 ID를 전달하여 수정 처리
	    // 서비스에서 DB를 업데이트하고 최신 정보가 담긴 DTO를 반환합니다.
	    UserReviewDTO updatedReview = userReviewService.updateReview(reviewId, updateDto, cud.getUserId());
	    
	    // 2. HTTP 200 OK와 함께 수정된 리뷰 데이터를 반환
	    return ResponseEntity.ok(updatedReview);
	}

	
}
