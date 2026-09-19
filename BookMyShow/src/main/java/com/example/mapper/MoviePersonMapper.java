package com.example.mapper;

import com.example.dto.request.MoviePersonCreateRequest;
import com.example.dto.request.response.MoviePersonResponse;
import com.example.entity.MoviePerson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MoviePersonMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "movie", ignore = true)
    @Mapping(target = "person", ignore = true)
    MoviePerson toEntity(MoviePersonCreateRequest request);

    @Mapping(target = "movieId", source = "movie.id")
    @Mapping(target = "personId", source = "person.id")
    MoviePersonResponse toResponse(MoviePerson moviePerson);
}
