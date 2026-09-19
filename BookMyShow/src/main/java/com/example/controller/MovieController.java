package com.example.controller;

import com.example.dto.request.MovieCreateRequest;
import com.example.dto.request.MovieUpdateRequest;
import com.example.dto.request.response.MovieResponse;
import com.example.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public MovieResponse createMovie(
            @RequestBody MovieCreateRequest request) {
        return movieService.createMovie(request);
    }

    @GetMapping("/{id}")
    public MovieResponse getMovieById(
            @PathVariable Long id) {
        return movieService.getMovieById(id);
    }

    @GetMapping
    public List<MovieResponse> getAllMovies() {
        return movieService.getAllMovies();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public MovieResponse updateMovie(
            @PathVariable Long id,
            @RequestBody MovieUpdateRequest request) {
        return movieService.updateMovie(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void archiveMovie(@PathVariable Long id) {
        movieService.archiveMovie(id);
    }
}
