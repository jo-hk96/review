package com.review.service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.review.entity.movieEntity;
import com.review.repository.MovieRepository;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class MovieService {
	

    private final MovieRepository movieRepository;
    
    @Transactional
    public movieEntity saveIfNotExist(Long apiId) {
        return movieRepository.findByApiId(apiId)
                .orElseGet(() -> {
                    movieEntity newMovie = movieEntity.builder()
                            .apiId(apiId)
                            .build(); 
                    return movieRepository.save(newMovie);
                });
    		}
		}
		
		
