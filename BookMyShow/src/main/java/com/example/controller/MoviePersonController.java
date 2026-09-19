package com.example.controller;

import com.example.dto.request.MoviePersonCreateRequest;
import com.example.dto.request.response.MoviePersonResponse;
import com.example.service.MoviePersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movie-persons")
@RequiredArgsConstructor
public class MoviePersonController {

    private final MoviePersonService moviePersonService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<MoviePersonResponse> createMoviePerson(
            @RequestBody MoviePersonCreateRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(moviePersonService.createMoviePerson(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MoviePersonResponse> getMoviePersonById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                moviePersonService.getMoviePersonById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<MoviePersonResponse>>
    getAllMoviePersons() {

        return ResponseEntity.ok(
                moviePersonService.getAllMoviePersons()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMoviePerson(
            @PathVariable Long id) {

        moviePersonService.deleteMoviePerson(id);

        return ResponseEntity.noContent().build();
    }
}
