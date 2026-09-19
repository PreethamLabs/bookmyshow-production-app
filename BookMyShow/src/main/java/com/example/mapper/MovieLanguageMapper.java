package com.example.mapper;

import com.example.dto.request.MovieLanguageCreateRequest;
import com.example.dto.request.response.MovieLanguageResponse;
import com.example.entity.MovieLanguage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovieLanguageMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "movie", ignore = true)
    @Mapping(target = "language", ignore = true)
    MovieLanguage toEntity(MovieLanguageCreateRequest request);

    @Mapping(target = "movieId", source = "movie.id")
    @Mapping(target = "languageId", source = "language.id")
    MovieLanguageResponse toResponse(MovieLanguage movieLanguage);
}
