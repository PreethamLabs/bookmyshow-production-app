package com.example.paymentclient;

import com.example.paymentclient.dto.PaymentServiceCreateRequest;
import com.example.paymentclient.dto.PaymentServiceResponse;
import com.example.paymentclient.dto.PaymentServiceVerifyRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class PaymentServiceClientImpl implements PaymentServiceClient {

    private final RestClient restClient;
    private final String serviceToken;

    public PaymentServiceClientImpl(
            RestClient paymentServiceRestClient,
            @Value("${payment.service.token}") String serviceToken) {

        this.restClient = paymentServiceRestClient;
        this.serviceToken = serviceToken;

        System.out.println(
                "MAIN APP - service token loaded: "
                        + (serviceToken != null ? "YES" : "NO")
        );
    }

    @Override
    public PaymentServiceResponse createPayment(
            PaymentServiceCreateRequest request) {


        return restClient.post()
                .uri("/api/payments")
                .header("X-Service-Token", serviceToken)
                .body(request)
                .retrieve()
                .body(PaymentServiceResponse.class);

    }

    @Override
    public PaymentServiceResponse refundPayment(Long paymentId) {

        System.out.println(
                "MAIN APP - sending service token: "
                        + (serviceToken != null ? "YES" : "NO")
        );



        return restClient.post()
                .uri("/internal/payments/{paymentId}/refund", paymentId)
                .header("X-Service-Token", serviceToken)
                .retrieve()
                .body(PaymentServiceResponse.class);
    }

    @Override
    public PaymentServiceResponse verifyPayment(
            PaymentServiceVerifyRequest request) {
        return restClient.post()
                .uri("/api/payments/verify")
                .header("X-Service-Token", serviceToken)
                .body(request)
                .retrieve()
                .body(PaymentServiceResponse.class);
    }
}