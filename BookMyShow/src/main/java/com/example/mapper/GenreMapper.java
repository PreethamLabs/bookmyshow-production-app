package com.example.mapper;

import com.example.dto.request.GenreCreateRequest;
import com.example.dto.request.response.GenreResponse;
import com.example.entity.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    @Mapping(target = "id", ignore = true)
    Genre toEntity(GenreCreateRequest request);

    GenreResponse toResponse(Genre genre);
}
