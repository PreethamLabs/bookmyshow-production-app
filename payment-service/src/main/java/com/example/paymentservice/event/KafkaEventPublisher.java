package com.example.paymentservice.event;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
public class KafkaEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void publish(OutboxEvent event) throws ExecutionException, InterruptedException {

        kafkaTemplate.send(
                "payment-events",
                event.getEventId(),
                event.getPayload()
        ).get();
    }
}
