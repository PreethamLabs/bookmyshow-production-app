package com.example.service;

import com.example.dto.request.MovieLanguageCreateRequest;
import com.example.dto.request.response.MovieLanguageResponse;

import java.util.List;

public interface MovieLanguageService {

    MovieLanguageResponse createMovieLanguage(
            MovieLanguageCreateRequest request);

    MovieLanguageResponse getMovieLanguageById(Long id);

    List<MovieLanguageResponse> getAllMovieLanguages();

    void deleteMovieLanguage(Long id);
}
