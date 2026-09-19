package com.example.paymentservice.service;

import com.example.paymentservice.dto.PaymentCreateRequest;
import com.example.paymentservice.dto.PaymentResponse;
import com.example.paymentservice.dto.PaymentVerifyRequest;


public interface PaymentService {

    PaymentResponse createPayment(
            PaymentCreateRequest request
    );

    PaymentResponse verifyPayment(PaymentVerifyRequest request);

    PaymentResponse markPaymentFailed(Long paymentId);

    void processPaymentCaptured(
            String razorpayOrderId,
            String razorpayPaymentId
    );

    PaymentResponse refundPayment(Long paymentId);
}
