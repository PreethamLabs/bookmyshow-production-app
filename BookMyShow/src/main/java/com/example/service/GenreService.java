package com.example.service;

import com.example.dto.request.GenreCreateRequest;
import com.example.dto.request.response.GenreResponse;
import org.springframework.stereotype.Service;

import java.util.List;


public interface GenreService {
    GenreResponse createGenre(GenreCreateRequest request);

    GenreResponse getGenreById(Long id);

    List<GenreResponse> getAllGenres();
}
