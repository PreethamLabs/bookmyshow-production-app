package com.example.paymentservice.client.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class BookingServiceConfig {

    @Value("${booking.service.url}")
    private String bookingServiceUrl;

    @Bean
    public RestClient bookingServiceRestClient() {
        return RestClient.builder()
                .baseUrl(bookingServiceUrl)
                .build();
    }
}
