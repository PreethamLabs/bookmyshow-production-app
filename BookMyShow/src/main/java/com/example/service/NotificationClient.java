package com.example.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_GATEWAY;

@Component
public class NotificationClient {

    private final RestClient client;

    public NotificationClient(
            @Value("${notification.service.url}") String baseUrl) {
        this.client = RestClient.builder().baseUrl(baseUrl).build();
    }

    public void sendOtp(String email) {
        try {
            client.post()
                    .uri("/api/otp/send")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("email", email))
                    .retrieve()
                    .toBodilessEntity();
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(
                    BAD_GATEWAY, "Unable to send email verification code", ex);
        }
    }

    public boolean verifyOtp(String email, String otp) {
        try {
            OtpResponse response = client.post()
                    .uri("/api/otp/verify")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("email", email, "otp", otp))
                    .retrieve()
                    .body(OtpResponse.class);
            return response != null && response.verified();
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(
                    BAD_GATEWAY, "Unable to verify email verification code", ex);
        }
    }

    private record OtpResponse(boolean verified, String message) {}
}
