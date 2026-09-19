package com.example.service;

import com.example.dto.request.MoviePersonCreateRequest;
import com.example.dto.request.response.MoviePersonResponse;

import java.util.List;

public interface MoviePersonService {

    MoviePersonResponse createMoviePerson(
            MoviePersonCreateRequest request);

    MoviePersonResponse getMoviePersonById(Long id);

    List<MoviePersonResponse> getAllMoviePersons();

    void deleteMoviePerson(Long id);
}
