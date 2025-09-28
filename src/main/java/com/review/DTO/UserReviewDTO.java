package com.review.DTO;


import java.time.format.DateTimeFormatter;

import com.review.entity.userReviewEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserReviewDTO {
	//유저 한줄평 영화 별점 저장
		private Long reviewId;      // DB에서 생성된 ID (응답에 필수)
	    private String nickname;    // 작성자 닉네임
	    private String comment;     
	    private int rating;
	    private String regDate;     // 포맷된 작성일 (응답에 필수)
		private Long movieId;    
	    
	    public static UserReviewDTO fromEntity(userReviewEntity entity) {
	        // 엔티티를 DTO로 변환하는 로직:
	        return UserReviewDTO.builder()
	                .reviewId(entity.getReviewId())
	                .nickname(entity.getUserEntity().getNickname()) // ⭐ 관계를 통해 닉네임 확보
	                .comment(entity.getComment())
	                .rating(entity.getRating())
	                .regDate(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(entity.getRegDate()))
	                .build();
	    }

	    
}
