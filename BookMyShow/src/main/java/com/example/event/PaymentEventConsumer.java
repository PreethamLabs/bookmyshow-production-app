package com.example.event;

import com.example.enums.BookingStatus;
import com.example.paymentclient.PaymentServiceClient;
import com.example.repository.ProcessedEventRepository;
import com.example.service.BookingService;
import com.example.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PaymentEventConsumer {

    private final BookingService bookingService;
    private final ObjectMapper objectMapper;
    private final ProcessedEventRepository processedEventRepository;
    private final TicketService ticketService;
    private final PaymentServiceClient paymentServiceClient;

    @KafkaListener(
            topics = "payment-events",
            groupId = "bookmyshow-booking-service"
    )
    @Transactional
    public void consume(String message) {

        try {
            PaymentSuccessEvent event =
                    objectMapper.readValue(
                            message,
                            PaymentSuccessEvent.class
                    );

            BookingStatus status =
                    bookingService.getBookingStatus(
                            event.getBookingId()
                    );


            if (status == BookingStatus.PENDING_PAYMENT) {

                bookingService.markPaymentSuccessful(
                        event.getBookingId()
                );

                ticketService.generateTicket(
                        event.getBookingId()
                );

            } else if (status == BookingStatus.EXPIRED) {

                paymentServiceClient.refundPayment(
                        event.getPaymentId()
                );


            } else if (status == BookingStatus.CONFIRMED) {

                return;

            } else {


                throw new RuntimeException(
                        "Unexpected booking status: " + status
                );
            }

            processedEventRepository.save(
                    ProcessedEvent.builder()
                            .eventId(event.getEventId())
                            .eventType("PAYMENT_SUCCESS")
                            .processedAt(LocalDateTime.now())
                            .build()
            );

        } catch (JacksonException e) {
            throw new RuntimeException(
                    "Failed to deserialize payment event",
                    e
            );
        }
    }
}
