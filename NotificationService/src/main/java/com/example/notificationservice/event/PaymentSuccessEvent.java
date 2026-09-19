package com.example.notificationservice.event;

import com.example.notificationservice.enums.PaymentEventType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentSuccessEvent {

    private String eventId;

    private Long paymentId;

    private Long bookingId;

    private Long userId;

    private String email;

    private BigDecimal amount;

    private String razorpayOrderId;

    private String razorpayPaymentId;

    private LocalDateTime paidAt;

    private PaymentEventType eventType;
}
