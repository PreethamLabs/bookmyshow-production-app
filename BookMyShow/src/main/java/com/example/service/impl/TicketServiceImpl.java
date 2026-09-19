package com.example.service.impl;

import com.example.dto.request.response.TicketResponse;
import com.example.entity.Booking;
import com.example.entity.Ticket;
import com.example.enums.BookingStatus;
import com.example.mapper.TicketMapper;
import com.example.repository.BookingRepository;
import com.example.repository.TicketRepository;
import com.example.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final BookingRepository bookingRepository;

    @Override
    public TicketResponse getTicketById(Long id) {

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        return ticketMapper.toResponse(ticket);
    }

    @Override
    public TicketResponse getTicketByBooking(Long bookingId) {

        Ticket ticket = ticketRepository.findTicketByBookingId(bookingId);

        if (ticket == null) {
            throw new RuntimeException("Ticket not found");
        }

        return ticketMapper.toResponse(ticket);
    }

    @Override
    @Transactional
    public TicketResponse generateTicket(Long bookingId) {

        Ticket existingTicket =
                ticketRepository.findTicketByBookingId(bookingId);

        if (existingTicket != null) {
            return ticketMapper.toResponse(existingTicket);
        }

        Booking booking =
                bookingRepository.findById(bookingId)
                        .orElseThrow(() ->
                                new RuntimeException("Booking not found"));

        if (booking.getBookingStatus() != BookingStatus.CONFIRMED) {
            throw new RuntimeException(
                    "Ticket can only be generated for confirmed booking"
            );
        }

        Ticket ticket = Ticket.builder()
                .booking(booking)
                .ticketNumber(generateTicketNumber())
                .build();

        Ticket savedTicket = ticketRepository.save(ticket);

        return ticketMapper.toResponse(savedTicket);
    }

    private String generateTicketNumber() {
        return "BMS-" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12)
                .toUpperCase();
    }
}
