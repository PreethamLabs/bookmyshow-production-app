package com.example.mapper;

import com.example.dto.request.MovieGenreCreateRequest;
import com.example.dto.request.response.MovieGenreResponse;
import com.example.entity.MovieGenre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovieGenreMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "movie", ignore = true)
    @Mapping(target = "genre", ignore = true)
    MovieGenre toEntity(MovieGenreCreateRequest request);

    @Mapping(target = "movieId", source = "movie.id")
    @Mapping(target = "genreId", source = "genre.id")
    MovieGenreResponse toResponse(MovieGenre movieGenre);
}
