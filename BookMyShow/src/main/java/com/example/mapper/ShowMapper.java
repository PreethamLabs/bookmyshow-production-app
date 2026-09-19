package com.example.mapper;

import com.example.dto.request.ShowCreateRequest;
import com.example.dto.request.ShowUpdateRequest;
import com.example.dto.request.response.ShowResponse;
import com.example.entity.Show;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ShowMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "movie", ignore = true)
    @Mapping(target = "screen", ignore = true)
    Show toEntity(ShowCreateRequest request);

    @Mapping(target = "movieId", source = "movie.id")
    @Mapping(target = "screenId", source = "screen.id")
    ShowResponse toResponse(Show show);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "movie", ignore = true)
    @Mapping(target = "screen", ignore = true)
    void updateEntity(
            ShowUpdateRequest request,
            @MappingTarget Show show
    );
}
