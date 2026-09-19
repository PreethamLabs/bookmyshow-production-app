package com.example.paymentservice.event;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentLateRefundedEvent {

    private String eventId;

    private Long paymentId;
    private Long bookingId;

    private Long userId;
    private String email;

    private BigDecimal amount;

    private PaymentEventType eventType;

    private String razorpayPaymentId;

    private LocalDateTime refundedAt;

    private String reason;
}
