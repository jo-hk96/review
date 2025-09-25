package com.review.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "USER_REVIEW")
@Data
public class userReviewEntity {
		
	
		//유저 한줄평 영화 별점 저장
		@SequenceGenerator(
		    name = "REVIEW_SEQ_GENERATOR", // ⭐ 또 다른 Generator 이름
		    sequenceName = "REVIEW_SEQ",   // ⭐ DB 시퀀스 이름도 다르게!
		    allocationSize = 1
		)
		@Id
		@GeneratedValue(
		    strategy = GenerationType.SEQUENCE, 
		    generator = "REVIEW_SEQ_GENERATOR"
		)
	    private Long reviewId; // 리뷰 기본키
	
	    private String comment; // 리뷰 코멘트
	
	    private int rating; //별점
	
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "USER_ID") // 실제 DB 컬럼명
	    private userEntity userEntity; // user 엔티티와 연결
	
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "MOVIE_ID") // 실제 DB 컬럼명
	    private movieEntity movieEntity; // Movie 엔티티와 연결
}
