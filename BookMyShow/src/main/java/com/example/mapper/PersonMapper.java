package com.example.mapper;

import com.example.dto.request.PersonCreateRequest;
import com.example.dto.request.response.PersonResponse;
import com.example.entity.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    @Mapping(target = "id", ignore = true)
    Person toEntity(PersonCreateRequest request);

    PersonResponse toResponse(Person person);
}
