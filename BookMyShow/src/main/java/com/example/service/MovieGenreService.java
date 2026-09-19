package com.example.service;

import com.example.dto.request.MovieGenreCreateRequest;
import com.example.dto.request.response.MovieGenreResponse;

import java.util.List;

public interface MovieGenreService {


    MovieGenreResponse createMovieGenre(MovieGenreCreateRequest request);

    MovieGenreResponse getMovieGenreById(Long id);

    List<MovieGenreResponse> getAllMovieGenres();

    void deleteMovieGenre(Long id);
}