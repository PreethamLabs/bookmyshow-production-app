package com.example.service;


import com.example.dto.request.response.TicketResponse;
import org.springframework.stereotype.Service;

public interface TicketService {

    TicketResponse generateTicket(Long bookingId);

    TicketResponse getTicketById(Long id);

    TicketResponse getTicketByBooking(Long bookingId);
}
