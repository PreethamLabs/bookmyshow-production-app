package com.example.mapper;

import com.example.dto.request.TheatreCreateRequest;
import com.example.dto.request.TheatreUpdateRequest;
import com.example.dto.request.response.TheatreResponse;
import com.example.entity.Theatre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TheatreMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "city", ignore = true)
    Theatre toEntity(TheatreCreateRequest request);

    @Mapping(target = "cityId", source = "city.id")
    TheatreResponse toResponse(Theatre theatre);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "city", ignore = true)
    void updateEntity(TheatreUpdateRequest request, @MappingTarget Theatre theatre);
}
