package com.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.review.entity.movieEntity;

@Repository
public interface MovieRepository extends JpaRepository<movieEntity ,Long> {

}
