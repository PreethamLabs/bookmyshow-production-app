package com.example.service.impl;

import com.example.dto.request.MovieLanguageCreateRequest;
import com.example.dto.request.response.MovieLanguageResponse;
import com.example.entity.Language;
import com.example.entity.Movie;
import com.example.entity.MovieLanguage;
import com.example.mapper.MovieLanguageMapper;
import com.example.repository.LanguageRepository;
import com.example.repository.MovieLanguageRepository;
import com.example.repository.MovieRepository;
import com.example.service.MovieLanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class MovieLanguageServiceImpl implements MovieLanguageService {

    private final MovieLanguageRepository movieLanguageRepository;
    private final MovieRepository movieRepository;
    private final LanguageRepository languageRepository;
    private final MovieLanguageMapper movieLanguageMapper;

    @Override
    public MovieLanguageResponse createMovieLanguage(
            MovieLanguageCreateRequest request) {

        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Movie not found with id: "
                                        + request.getMovieId()));

        Language language = languageRepository
                .findById(request.getLanguageId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Language not found with id: "
                                        + request.getLanguageId()));

        MovieLanguage movieLanguage =
                movieLanguageMapper.toEntity(request);

        movieLanguage.setMovie(movie);
        movieLanguage.setLanguage(language);

        MovieLanguage savedMovieLanguage =
                movieLanguageRepository.save(movieLanguage);

        return movieLanguageMapper.toResponse(savedMovieLanguage);
    }

    @Override
    public MovieLanguageResponse getMovieLanguageById(Long id) {

        MovieLanguage movieLanguage =
                movieLanguageRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "MovieLanguage not found with id: "
                                                + id));

        return movieLanguageMapper.toResponse(movieLanguage);
    }

    @Override
    public List<MovieLanguageResponse> getAllMovieLanguages() {

        return movieLanguageRepository.findAll()
                .stream()
                .map(movieLanguageMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteMovieLanguage(Long id) {

        if (!movieLanguageRepository.existsById(id)) {
            throw new RuntimeException(
                    "MovieLanguage not found with id: " + id);
        }

        movieLanguageRepository.deleteById(id);
    }
}
