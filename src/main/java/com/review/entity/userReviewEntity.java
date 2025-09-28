package com.review.entity;


import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USER_REVIEW")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class userReviewEntity {
		
	
		//유저 한줄평 영화 별점 저장
		@SequenceGenerator(
		    name = "REVIEW_SEQ_GENERATOR", 
		    sequenceName = "REVIEW_SEQ",   
		    allocationSize = 1
		)
		@Id
		@GeneratedValue(
		    strategy = GenerationType.SEQUENCE, 
		    generator = "REVIEW_SEQ_GENERATOR"
		)
		@Column(name = "REVIEWID")
	    private Long reviewId; // 리뷰 기본키
		
		@Column(name = "COMMENT",nullable = false, length = 500)
	    private String comment; // 리뷰 코멘트
		
		@Column(name = "RATING",nullable = false)
	    private int rating; //별점
	
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "USER_ID") // 실제 DB 컬럼명
	    private userEntity userEntity; // user 엔티티와 연결
	
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "MOVIE_ID") // 실제 DB 컬럼명
	    private movieEntity movieEntity; // Movie 엔티티와 연결
	    
	    
	    
	    @Builder.Default
	    @Column(name = "REGDATE" ,updatable = false) // 최초 생성 후 업데이트 방지
	    private LocalDateTime regDate = LocalDateTime.now();
	    
	   
}
