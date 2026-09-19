package com.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class PaymentServiceConfig {

    @Value("${payment.service.url}")
    private String paymentServiceUrl;

    @Bean
    public RestClient paymentServiceRestClient() {
        return RestClient.builder()
                .baseUrl(paymentServiceUrl)
                .build();
    }
}
