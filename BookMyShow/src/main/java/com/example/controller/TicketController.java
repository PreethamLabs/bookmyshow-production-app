package com.example.controller;

import com.example.dto.request.response.TicketResponse;
import com.example.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/{id}")
    public TicketResponse getTicketById(
            @PathVariable Long id) {

        return ticketService.getTicketById(id);
    }

    @GetMapping("/booking/{bookingId}")
    public TicketResponse getTicketByBooking(
            @PathVariable Long bookingId) {

        return ticketService.getTicketByBooking(bookingId);
    }
}
