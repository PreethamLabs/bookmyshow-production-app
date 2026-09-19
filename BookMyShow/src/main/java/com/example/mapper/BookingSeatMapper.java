package com.example.mapper;

import com.example.dto.request.response.BookingSeatResponse;
import com.example.entity.BookingSeat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingSeatMapper {

    @Mapping(target = "bookingId", source = "booking.id")
    @Mapping(target = "showSeatId", source = "showSeat.id")
    @Mapping(target = "seatNumber", source = "showSeat.seat.seatNumber")
    @Mapping(target = "seatType", source = "showSeat.seat.seatType")
    BookingSeatResponse toResponse(BookingSeat bookingSeat);
}
