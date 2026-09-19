//package com.example.service;
//
//import com.example.dto.request.PaymentCreateRequest;
//import com.example.dto.request.response.PaymentResponse;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//public interface PaymentService {
//    PaymentResponse createPayment(String  email, PaymentCreateRequest request);
//
//    PaymentResponse getPaymentById(Long id);
//
//    List<PaymentResponse> getPaymentsByBooking(Long bookingId);
//
//    @Transactional
//    void markPaymentSuccessful(Long paymentId);
//}
