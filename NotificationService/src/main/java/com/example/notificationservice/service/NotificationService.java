package com.example.notificationservice.service;

import com.example.notificationservice.entity.NotificationLog;
import com.example.notificationservice.enums.NotificationStatus;
import com.example.notificationservice.event.PaymentLateRefundedEvent;
import com.example.notificationservice.event.PaymentSuccessEvent;
import com.example.notificationservice.repository.NotificationLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationLogRepository repository;
    private final EmailService emailService;


    @Transactional
    public void sendPaymentSuccessNotification(
            PaymentSuccessEvent event) {

        String emailMessage =
                """
                Your BookMyShow payment was successful.

                Booking ID: %s
                Amount: ₹%s

                Thank you for booking with BookMyShow.
                """.formatted(
                        event.getBookingId(),
                        event.getAmount()
                );

        sendEmail(
                event.getEventId(),
                event.getEmail(),
                "Payment Successful",
                emailMessage
        );

    }

    private void sendEmail(
            String eventId,
            String recipient,
            String subject,
            String message) {

        if (repository.existsByEventIdAndChannel(
                eventId,
                "EMAIL")) {
            return;
        }

        NotificationLog log =
                NotificationLog.builder()
                        .eventId(eventId)
                        .channel("EMAIL")
                        .recipient(recipient)
                        .message(message)
                        .status(NotificationStatus.PENDING)
                        .build();

        repository.save(log);

        try {

            emailService.sendEmail(
                    recipient,
                    subject,
                    message
            );

            log.setStatus(NotificationStatus.SENT);
            log.setSentAt(LocalDateTime.now());

        } catch (Exception e) {

            log.setStatus(NotificationStatus.FAILED);
            log.setFailureReason(e.getMessage());
        }

        repository.save(log);
    }

    @Transactional
    public void sendLateRefundNotification(
            PaymentLateRefundedEvent event) {

        String emailMessage =
                """
                Your BookMyShow booking has expired and your payment has been refunded.
    
                Booking ID: %s
                Amount: ₹%s
                Refunded At: %s
                Payment ID: %s
    
                Reason:
                %s
    
                The amount has been refunded to your original payment method.
    
                No seats were booked for this transaction.
                """.formatted(
                        event.getBookingId(),
                        event.getAmount(),
                        event.getRefundedAt(),
                        event.getRazorpayPaymentId(),
                        event.getReason()
                );

        sendEmail(
                event.getEventId(),
                event.getEmail(),
                "Payment Refunded - Booking Expired",
                emailMessage
        );
    }

}
