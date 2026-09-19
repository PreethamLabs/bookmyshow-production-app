package com.example.controller;

import com.example.dto.request.MovieGenreCreateRequest;
import com.example.dto.request.response.MovieGenreResponse;
import com.example.service.MovieGenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movie-genres")
@RequiredArgsConstructor
public class MovieGenreController {

    private final MovieGenreService movieGenreService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<MovieGenreResponse> createMovieGenre(
            @RequestBody MovieGenreCreateRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(movieGenreService.createMovieGenre(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieGenreResponse> getMovieGenreById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                movieGenreService.getMovieGenreById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<MovieGenreResponse>> getAllMovieGenres() {

        return ResponseEntity.ok(
                movieGenreService.getAllMovieGenres()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovieGenre(
            @PathVariable Long id) {

        movieGenreService.deleteMovieGenre(id);

        return ResponseEntity.noContent().build();
    }
}
