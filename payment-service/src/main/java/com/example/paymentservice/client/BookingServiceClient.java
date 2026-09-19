package com.example.paymentservice.client;

import com.example.paymentservice.dto.NotificationDetails;

import java.math.BigDecimal;

public interface BookingServiceClient {

    BigDecimal getBookingAmount(Long bookingId);

    NotificationDetails getNotificationDetails(Long bookingId);
}
