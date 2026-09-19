package com.example.paymentclient;

import com.example.paymentclient.dto.PaymentServiceCreateRequest;
import com.example.paymentclient.dto.PaymentServiceResponse;
import com.example.paymentclient.dto.PaymentServiceVerifyRequest;

public interface PaymentServiceClient {

    PaymentServiceResponse createPayment(
            PaymentServiceCreateRequest request
    );

    PaymentServiceResponse refundPayment(Long paymentId);

    PaymentServiceResponse verifyPayment(PaymentServiceVerifyRequest request);
}
