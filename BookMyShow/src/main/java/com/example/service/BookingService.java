package com.example.service;

import com.example.dto.request.BookingCreateRequest;
import com.example.dto.request.NotificationDetails;
import com.example.dto.request.response.BookingResponse;
import com.example.enums.BookingStatus;
import com.example.paymentclient.dto.PaymentServiceResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


public interface BookingService {

    BookingResponse createBooking(
            String email,
            BookingCreateRequest request
    );

    BookingResponse getBookingById(Long id, String email);

    List<BookingResponse> getBookingsByUserEmail(String email);

    BookingResponse cancelBooking(Long id, String email);

    BigDecimal getBookingAmount(Long bookingId);

    PaymentServiceResponse createPayment(Long bookingId, String email);

    void markPaymentSuccessful(Long bookingId);

    NotificationDetails getNotificationDetails(Long bookingId);

    BookingStatus getBookingStatus(Long bookingId);
}

