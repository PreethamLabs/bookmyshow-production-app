package com.example.mapper;

import com.example.dto.request.BookingCreateRequest;
import com.example.dto.request.response.BookingResponse;
import com.example.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "show", ignore = true)
    @Mapping(target = "bookingStatus", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "bookedAt", ignore = true)
    @Mapping(target = "cancelledAt", ignore = true)
    @Mapping(target = "lockToken", ignore = true)
    Booking toEntity(BookingCreateRequest request);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "showId", source = "show.id")
    BookingResponse toResponse(Booking booking);
}
