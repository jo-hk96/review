package com.review.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;
import java.io.Serializable;

@Embeddable 
@Data
public class MovieLikeId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    private Long userId;
    private Long movieId;
}