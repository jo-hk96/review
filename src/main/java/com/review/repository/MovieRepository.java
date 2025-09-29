package com.review.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.review.entity.movieEntity;

@Repository
public interface MovieRepository extends JpaRepository<movieEntity ,Long> {
	Optional<movieEntity> findByApiId(Long apiId); 
	Optional<movieEntity> findByMovieId(Long movieId);
}