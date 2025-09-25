package com.review.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "MOVIE_LIKE")
@Data
public class MovieLike {

    @EmbeddedId // 복합 키를 사용
    private MovieLikeId id;

    @ManyToOne
    @MapsId("userId") // MovieLikeId의 memberId와 매핑
    @JoinColumn(name = "USER_ID")
    private userEntity userEntity;

    @ManyToOne
    @MapsId("movieId") // MovieLikeId의 movieId와 매핑
    @JoinColumn(name = "MOVIE_ID")
    private movieEntity movieEntity;
}