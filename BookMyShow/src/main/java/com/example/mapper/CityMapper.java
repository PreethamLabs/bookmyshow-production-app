package com.example.mapper;

import com.example.dto.request.CityCreateRequest;
import com.example.dto.request.response.CityResponse;
import com.example.entity.City;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CityMapper {

    @Mapping(target = "id", ignore = true)
    City toEntity(CityCreateRequest request);

    CityResponse toResponse(City city);
}
