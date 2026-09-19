package com.example.service.impl;

import com.example.dto.request.GenreCreateRequest;
import com.example.dto.request.response.GenreResponse;
import com.example.entity.Genre;
import com.example.mapper.GenreMapper;
import com.example.repository.GenreRepository;
import com.example.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreMapper genreMapper;
    private final GenreRepository genreRepository;

    @Override
    public GenreResponse createGenre(GenreCreateRequest request) {

        Genre genre = genreMapper.toEntity(request);

        Genre savedGenre = genreRepository.save(genre);

        return genreMapper.toResponse(savedGenre);
    }

    @Override
    public GenreResponse getGenreById(Long id) {

        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Genre not found"));

        return genreMapper.toResponse(genre);
    }

    @Override
    public List<GenreResponse> getAllGenres() {

        return genreRepository.findAll()
                .stream()
                .map(genreMapper::toResponse)
                .toList();
    }
}
