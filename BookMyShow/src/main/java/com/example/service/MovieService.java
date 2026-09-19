package com.example.service;

import com.example.dto.request.MovieCreateRequest;
import com.example.dto.request.MovieUpdateRequest;
import com.example.dto.request.response.MovieResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface MovieService {

    MovieResponse createMovie(MovieCreateRequest request);

    MovieResponse getMovieById(Long id);

    List<MovieResponse> getAllMovies();

    MovieResponse updateMovie(Long id, MovieUpdateRequest request);

    void archiveMovie(Long id);

}
