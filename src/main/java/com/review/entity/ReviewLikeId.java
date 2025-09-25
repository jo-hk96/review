package com.review.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;
import java.io.Serializable;

@Embeddable // 다른 엔티티에 포함될 수 있는 클래스임을 나타냄
@Data
public class ReviewLikeId implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;
    private Long reviewId;
}