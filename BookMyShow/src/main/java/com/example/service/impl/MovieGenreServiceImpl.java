package com.example.service.impl;

import com.example.dto.request.MovieGenreCreateRequest;
import com.example.dto.request.response.MovieGenreResponse;
import com.example.entity.Genre;
import com.example.entity.Movie;
import com.example.entity.MovieGenre;
import com.example.mapper.MovieGenreMapper;
import com.example.repository.GenreRepository;
import com.example.repository.MovieGenreRepository;
import com.example.repository.MovieRepository;
import com.example.service.MovieGenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieGenreServiceImpl implements MovieGenreService {

    private final MovieGenreRepository movieGenreRepository;
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final MovieGenreMapper movieGenreMapper;

    @Override
    public MovieGenreResponse createMovieGenre(
            MovieGenreCreateRequest request) {

        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Movie not found with id: "
                                        + request.getMovieId()));

        Genre genre = genreRepository.findById(request.getGenreId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Genre not found with id: "
                                        + request.getGenreId()));

        MovieGenre movieGenre =
                movieGenreMapper.toEntity(request);

        movieGenre.setMovie(movie);
        movieGenre.setGenre(genre);

        MovieGenre savedMovieGenre =
                movieGenreRepository.save(movieGenre);

        return movieGenreMapper.toResponse(savedMovieGenre);
    }

    @Override
    public MovieGenreResponse getMovieGenreById(Long id) {

        MovieGenre movieGenre =
                movieGenreRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "MovieGenre not found with id: " + id));

        return movieGenreMapper.toResponse(movieGenre);
    }

    @Override
    public List<MovieGenreResponse> getAllMovieGenres() {

        return movieGenreRepository.findAll()
                .stream()
                .map(movieGenreMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteMovieGenre(Long id) {

        if (!movieGenreRepository.existsById(id)) {
            throw new RuntimeException(
                    "MovieGenre not found with id: " + id);
        }

        movieGenreRepository.deleteById(id);
    }
}