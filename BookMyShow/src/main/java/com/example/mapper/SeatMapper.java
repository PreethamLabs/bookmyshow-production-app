package com.example.mapper;

import com.example.dto.request.SeatCreateRequest;
import com.example.dto.request.SeatUpdateRequest;
import com.example.dto.request.response.SeatResponse;
import com.example.entity.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SeatMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "screen", ignore = true)
    Seat toEntity(SeatCreateRequest request);

    @Mapping(target = "screenId", source = "screen.id")
    SeatResponse toResponse(Seat seat);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "screen", ignore = true)
    void updateEntity(
            SeatUpdateRequest request,
            @MappingTarget Seat seat
    );
}
