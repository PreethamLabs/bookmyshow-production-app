package com.example.controller;


import com.example.dto.request.NotificationDetails;
import com.example.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/internal/bookings")
@RequiredArgsConstructor
public class BookingInternalController {

    private final BookingService bookingService;

    @GetMapping("/{bookingId}/amount")
    public BigDecimal getBookingAmount(
            @PathVariable Long bookingId) {

        return bookingService.getBookingAmount(bookingId);
    }

    @PostMapping("/{bookingId}/payment-success")
    public void markPaymentSuccessful(
            @PathVariable Long bookingId) {

        bookingService.markPaymentSuccessful(bookingId);
    }

    @GetMapping("/{bookingId}/notification-details")
    public NotificationDetails getNotificationDetails(
            @PathVariable Long bookingId) {

        return bookingService.getNotificationDetails(bookingId);
    }
}
