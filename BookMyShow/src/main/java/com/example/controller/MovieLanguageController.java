package com.example.controller;

import com.example.dto.request.MovieLanguageCreateRequest;
import com.example.dto.request.response.MovieLanguageResponse;
import com.example.service.MovieLanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/movie-languages")
@RequiredArgsConstructor
public class MovieLanguageController {

    private final MovieLanguageService movieLanguageService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<MovieLanguageResponse> createMovieLanguage(
            @RequestBody MovieLanguageCreateRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(movieLanguageService.createMovieLanguage(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieLanguageResponse> getMovieLanguageById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                movieLanguageService.getMovieLanguageById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<MovieLanguageResponse>>
    getAllMovieLanguages() {

        return ResponseEntity.ok(
                movieLanguageService.getAllMovieLanguages()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovieLanguage(
            @PathVariable Long id) {

        movieLanguageService.deleteMovieLanguage(id);

        return ResponseEntity.noContent().build();
    }
}
