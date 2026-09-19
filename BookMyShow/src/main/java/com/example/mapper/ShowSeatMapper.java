package com.example.mapper;

import com.example.dto.request.response.ShowSeatResponse;
import com.example.entity.ShowSeat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShowSeatMapper {

    @Mapping(target = "showId", source = "show.id")
    @Mapping(target = "seatId", source = "seat.id")
    @Mapping(target = "seatNumber", source = "seat.seatNumber")
    @Mapping(target = "seatType", source = "seat.seatType")
    ShowSeatResponse toResponse(ShowSeat showSeat);
}
