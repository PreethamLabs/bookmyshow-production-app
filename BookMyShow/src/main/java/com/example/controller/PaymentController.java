//package com.example.controller;
//
//import com.example.dto.request.PaymentCreateRequest;
//import com.example.dto.request.response.PaymentResponse;
//import com.example.security.SecurityUtils;
//import com.example.service.PaymentService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//
//@RestController
//@RequestMapping("/api/payments")
//@RequiredArgsConstructor
//public class PaymentController {
//
//    private final PaymentService paymentService;
//
//    @PostMapping
//    public PaymentResponse createPayment(
//            @RequestBody PaymentCreateRequest request) {
//
//        String email = SecurityUtils.getCurrentUserEmail();
//
//        return paymentService.createPayment(email, request);
//    }
//}
