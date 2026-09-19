package com.example.mapper;

import com.example.dto.request.ScreenCreateRequest;
import com.example.dto.request.ScreenUpdateRequest;
import com.example.dto.request.response.ScreenResponse;
import com.example.entity.Screen;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ScreenMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "theatre", ignore = true)
    Screen toEntity(ScreenCreateRequest request);

    @Mapping(target = "theatreId", source = "theatre.id")
    ScreenResponse toResponse(Screen screen);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "theatre", ignore = true)
    void updateEntity(
            ScreenUpdateRequest request,
            @MappingTarget Screen screen
    );
}
