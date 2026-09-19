package com.example.mapper;

import com.example.dto.request.MovieCreateRequest;
import com.example.dto.request.MovieUpdateRequest;
import com.example.dto.request.response.MovieResponse;
import com.example.entity.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MovieMapper {

    @Mapping(target = "id", ignore = true)
    Movie toEntity(MovieCreateRequest request);

    MovieResponse toResponse(Movie movie);

    @Mapping(target = "id", ignore = true)
    void updateEntity(MovieUpdateRequest request, @MappingTarget Movie movie);
}
