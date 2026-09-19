package com.example.controller;

import com.example.dto.request.BookingCreateRequest;
import com.example.dto.request.response.BookingResponse;
import com.example.paymentclient.dto.PaymentServiceResponse;
import com.example.paymentclient.dto.PaymentServiceVerifyRequest;
import com.example.paymentclient.PaymentServiceClient;
import com.example.security.SecurityUtils;
import com.example.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final PaymentServiceClient paymentServiceClient;

    @PostMapping
    public BookingResponse createBooking(
            @RequestBody BookingCreateRequest request) {

        String email = SecurityUtils.getCurrentUserEmail();

        return bookingService.createBooking(email, request);
    }

    @GetMapping("/{id}")
    public BookingResponse getBookingById(@PathVariable Long id) {

        String email = SecurityUtils.getCurrentUserEmail();

        return bookingService.getBookingById(id, email);
    }

    @GetMapping("/my")
    public List<BookingResponse> getMyBookings() {

        String email = SecurityUtils.getCurrentUserEmail();

        return bookingService.getBookingsByUserEmail(email);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<BookingResponse> cancelBooking(
            @PathVariable Long id) {

        String email = SecurityUtils.getCurrentUserEmail();

        return ResponseEntity.ok(
                bookingService.cancelBooking(id, email)
        );
    }

    @PostMapping("/{id}/payment")
    public PaymentServiceResponse createPayment(
            @PathVariable Long id) {

        String email = SecurityUtils.getCurrentUserEmail();

        return bookingService.createPayment(id, email);
    }

    @PostMapping("/payment/verify")
    public PaymentServiceResponse verifyPayment(
            @RequestBody PaymentServiceVerifyRequest request) {
        return paymentServiceClient.verifyPayment(request);
    }
}
