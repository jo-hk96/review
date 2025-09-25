package com.review.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "REVIEW_LIKE")
@Data
public class ReviewLike {

    @EmbeddedId // 복합 키를 사용
    private ReviewLikeId id;

    @ManyToOne
    @MapsId("userId") // ReviewLikeId의 memberId와 매핑
    @JoinColumn(name = "USER_ID")
    private userEntity userEntity;

    @ManyToOne
    @MapsId("reviewId") // ReviewLikeId의 reviewId와 매핑
    @JoinColumn(name = "REVIEW_ID")
    private userReviewEntity userReviewEntity;
}