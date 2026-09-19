package com.example.notificationservice.service;

import com.example.notificationservice.event.PaymentLateRefundedEvent;

public interface EmailService {

    void sendEmail(
            String to,
            String subject,
            String message
    );

}
