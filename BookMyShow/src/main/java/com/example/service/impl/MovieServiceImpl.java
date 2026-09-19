package com.example.service.impl;

import com.example.dto.request.MovieCreateRequest;
import com.example.dto.request.MovieUpdateRequest;
import com.example.dto.request.response.MovieResponse;
import com.example.entity.Movie;
import com.example.enums.MovieStatus;
import com.example.mapper.MovieMapper;
import com.example.repository.MovieRepository;
import com.example.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    @Override
    public MovieResponse createMovie(MovieCreateRequest request) {

        Movie movie = movieMapper.toEntity(request);

        Movie savedMovie = movieRepository.save(movie);

        return movieMapper.toResponse(savedMovie);
    }

    @Override
    public MovieResponse getMovieById(Long id) {

        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        return movieMapper.toResponse(movie);
    }

    @Override
    public List<MovieResponse> getAllMovies() {

        return movieRepository.findAll()
                .stream()
                .map(movieMapper::toResponse)
                .toList();
    }

    @Override
    public MovieResponse updateMovie(
            Long id,
            MovieUpdateRequest request) {

        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        movieMapper.updateEntity(request, movie);

        Movie updatedMovie = movieRepository.save(movie);

        return movieMapper.toResponse(updatedMovie);
    }

    @Override
    public void archiveMovie(Long id) {

        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        movie.setMovieStatus(MovieStatus.ARCHIVED);

        movieRepository.save(movie);
    }
}
