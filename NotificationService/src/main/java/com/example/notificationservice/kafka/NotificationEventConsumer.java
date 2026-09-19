package com.example.notificationservice.kafka;

import com.example.notificationservice.enums.PaymentEventType;
import com.example.notificationservice.event.PaymentLateRefundedEvent;
import com.example.notificationservice.event.PaymentSuccessEvent;
import com.example.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class NotificationEventConsumer {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "payment-events",
            groupId = "bookmyshow-notification-service"
    )
    public void consume(String message) {

        try {

            JsonNode root =
                    objectMapper.readTree(message);

            PaymentEventType eventType =
                    PaymentEventType.valueOf(
                            root.get("eventType").asText()
                    );

            switch (eventType) {

                case PAYMENT_SUCCESS -> {

                    PaymentSuccessEvent event =
                            objectMapper.treeToValue(
                                    root,
                                    PaymentSuccessEvent.class
                            );

                    notificationService
                            .sendPaymentSuccessNotification(event);
                }

                case PAYMENT_LATE_REFUNDED -> {

                    PaymentLateRefundedEvent event =
                            objectMapper.treeToValue(
                                    root,
                                    PaymentLateRefundedEvent.class
                            );

                    notificationService
                            .sendLateRefundNotification(event);
                }

                default -> {
                    // Ignore events not handled by
                    // Notification Service.
                }
            }

        } catch (JacksonException e) {

            throw new RuntimeException(
                    "Failed to process notification event",
                    e
            );
        }
    }
}