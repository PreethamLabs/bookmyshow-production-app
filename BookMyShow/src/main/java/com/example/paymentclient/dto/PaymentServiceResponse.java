package com.example.paymentclient.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentServiceResponse {

        private Long id;
        private Long bookingId;
        private String paymentStatus;
        private BigDecimal amount;
        private String razorpayOrderId;
        private String razorpayPaymentId;
        private LocalDateTime createdAt;

}

