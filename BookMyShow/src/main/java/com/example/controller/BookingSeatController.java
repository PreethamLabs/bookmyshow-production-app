package com.example.controller;

import com.example.dto.request.response.BookingSeatResponse;
import com.example.service.BookingSeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking-seats")
@RequiredArgsConstructor
public class BookingSeatController {

    private final BookingSeatService bookingSeatService;

    @GetMapping("/{id}")
    public BookingSeatResponse getBookingSeatById(
            @PathVariable Long id) {

        return bookingSeatService.getBookingSeatById(id);
    }

    @GetMapping("/booking/{bookingId}")
    public List<BookingSeatResponse> getBookingSeatsByBooking(
            @PathVariable Long bookingId) {

        return bookingSeatService.getBookingSeatsByBooking(bookingId);
    }
}
