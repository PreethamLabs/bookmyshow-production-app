package com.example.paymentservice.controller;


import com.example.paymentservice.dto.PaymentCreateRequest;
import com.example.paymentservice.dto.PaymentResponse;
import com.example.paymentservice.dto.PaymentVerifyRequest;
import com.example.paymentservice.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public PaymentResponse createPayment(
            @Valid @RequestBody PaymentCreateRequest request) {

        return paymentService.createPayment(request);
    }

    @PostMapping("/verify")
    public PaymentResponse verifyPayment(
            @Valid @RequestBody PaymentVerifyRequest request) {

        return paymentService.verifyPayment(request);
    }

}
