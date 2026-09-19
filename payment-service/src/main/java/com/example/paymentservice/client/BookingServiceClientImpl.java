package com.example.paymentservice.client;

import com.example.paymentservice.dto.NotificationDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

@Component
public class BookingServiceClientImpl
        implements BookingServiceClient {

    private final RestClient bookingServiceRestClient;
    private final String serviceToken;

    public BookingServiceClientImpl(
            RestClient bookingServiceRestClient,
            @Value("${payment.service.token}") String serviceToken) {

        this.bookingServiceRestClient = bookingServiceRestClient;
        this.serviceToken = serviceToken;
    }

    @Override
    public BigDecimal getBookingAmount(Long bookingId) {

        return bookingServiceRestClient
                .get()
                .uri("/internal/bookings/{id}/amount", bookingId)
                .header("X-Service-Token", serviceToken)
                .retrieve()
                .body(BigDecimal.class);
    }


    @Override
    public NotificationDetails getNotificationDetails(Long bookingId) {

        return bookingServiceRestClient
                .get()
                .uri(
                        "/internal/bookings/{id}/notification-details",
                        bookingId
                )
                .header("X-Service-Token", serviceToken)
                .retrieve()
                .body(NotificationDetails.class);
    }
}
