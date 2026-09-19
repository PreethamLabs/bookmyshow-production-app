package com.example.controller;

import com.example.dto.request.GenreCreateRequest;
import com.example.dto.request.response.GenreResponse;
import com.example.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/api/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<GenreResponse> createGenre(
            @RequestBody GenreCreateRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(genreService.createGenre(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreResponse> getGenreById( @PathVariable Long id) {
        return ResponseEntity.ok( genreService.getGenreById(id) );
    }

    @GetMapping public ResponseEntity<List<GenreResponse>> getAllGenres() {
        return ResponseEntity.ok( genreService.getAllGenres() );
    }
}
