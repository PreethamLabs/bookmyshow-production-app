package com.example.mapper;

import com.example.dto.request.response.TicketResponse;
import com.example.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    @Mapping(target = "bookingId", source = "booking.id")
    TicketResponse toResponse(Ticket ticket);
}
