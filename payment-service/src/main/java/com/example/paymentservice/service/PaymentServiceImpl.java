package com.example.paymentservice.service;

import com.example.paymentservice.client.BookingServiceClient;
import com.example.paymentservice.dto.NotificationDetails;
import com.example.paymentservice.dto.PaymentCreateRequest;
import com.example.paymentservice.dto.PaymentResponse;
import com.example.paymentservice.dto.PaymentVerifyRequest;
import com.example.paymentservice.entity.Payment;
import com.example.paymentservice.enums.PaymentStatus;
import com.example.paymentservice.event.*;
import com.example.paymentservice.mapper.PaymentMapper;
import com.example.paymentservice.repository.PaymentRepository;
import com.razorpay.*;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final RazorpayClient razorpayClient;
    private final BookingServiceClient bookingServiceClient;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;


    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;



    @Override
    @Transactional
    public PaymentResponse createPayment(
            PaymentCreateRequest request) {

        var existingPayment =
                paymentRepository.findByBookingIdAndPaymentStatusIn(
                        request.getBookingId(),
                        List.of(
                                PaymentStatus.CREATED,
                                PaymentStatus.PROCESSING
                        )
                );

        if (existingPayment.isPresent()) {
            return paymentMapper.toResponse(
                    existingPayment.get()
            );
        }

        BigDecimal amount =
                bookingServiceClient.getBookingAmount(
                        request.getBookingId()
                );

        try {
            JSONObject options = new JSONObject();

            options.put(
                    "amount",amount
                            .multiply(java.math.BigDecimal.valueOf(100))
                            .longValue()
            );

            options.put("currency", "INR");

            options.put(
                    "receipt",
                    "booking_" + request.getBookingId()
            );

            Order razorpayOrder =
                    razorpayClient.orders.create(options);

            Payment payment = Payment.builder()
                    .bookingId(request.getBookingId())
                    .paymentStatus(PaymentStatus.CREATED)
                    .amount(amount)
                    .razorpayOrderId(
                            razorpayOrder.get("id")
                    )
                    .build();

            Payment savedPayment =
                    paymentRepository.save(payment);

            return paymentMapper.toResponse(savedPayment);

        } catch (RazorpayException e) {

            throw new RuntimeException(
                    "Failed to create Razorpay order",
                    e
            );
        }
    }

    @Override
    @Transactional
    public PaymentResponse verifyPayment(PaymentVerifyRequest request) {



        Payment payment = paymentRepository.findByRazorpayOrderIdForUpdate(
                        request.getRazorpayOrderId()
                )
                .orElseThrow(() ->
                        new RuntimeException("Payment not found"));

        if (payment.getPaymentStatus() == PaymentStatus.SUCCESS) {
            return paymentMapper.toResponse(payment);
        }

        if (payment.getPaymentStatus() != PaymentStatus.CREATED
                && payment.getPaymentStatus() != PaymentStatus.PROCESSING) {
            throw new RuntimeException(
                    "Payment cannot be verified in current state"
            );
        }

        try {
            JSONObject attributes = new JSONObject();

            attributes.put(
                    "razorpay_order_id",
                    request.getRazorpayOrderId()
            );

            attributes.put(
                    "razorpay_payment_id",
                    request.getRazorpayPaymentId()
            );

            attributes.put(
                    "razorpay_signature",
                    request.getRazorpaySignature()
            );

            boolean valid =
                    Utils.verifyPaymentSignature(
                            attributes,
                            razorpayKeySecret
                    );

            if (!valid) {
                throw new RuntimeException("Invalid payment signature");
            }

            payment.setRazorpayPaymentId(
                    request.getRazorpayPaymentId()
            );

            payment.setPaymentStatus(PaymentStatus.SUCCESS);
            payment.setPaidAt(LocalDateTime.now());

            Payment savedPayment =
                    paymentRepository.save(payment);

            NotificationDetails notificationDetails =
                    bookingServiceClient.getNotificationDetails(
                            savedPayment.getBookingId()
                    );

            PaymentSuccessEvent event = PaymentSuccessEvent.builder()
                    .eventId(UUID.randomUUID().toString())
                    .paymentId(savedPayment.getId())
                    .bookingId(savedPayment.getBookingId())
                    .userId(notificationDetails.getUserId())
                    .email(notificationDetails.getEmail())
                    .amount(savedPayment.getAmount())
                    .eventType(PaymentEventType.PAYMENT_SUCCESS)
                    .razorpayOrderId(savedPayment.getRazorpayOrderId())
                    .razorpayPaymentId(savedPayment.getRazorpayPaymentId())
                    .paidAt(savedPayment.getPaidAt())
                    .build();

            try {
                String payload = objectMapper.writeValueAsString(event);

                OutboxEvent outboxEvent = OutboxEvent.builder()
                        .eventId(event.getEventId())
                        .eventType(PaymentEventType.PAYMENT_SUCCESS)
                        .bookingId(event.getBookingId())
                        .payload(payload)
                        .published(false)
                        .createdAt(LocalDateTime.now())
                        .build();

                outboxEventRepository.save(outboxEvent);

            } catch (JacksonException e) {
                throw new RuntimeException("Failed to create payment event", e);
            }


            return paymentMapper.toResponse(savedPayment);

        } catch (RazorpayException e) {
            throw new RuntimeException(
                    "Payment signature verification failed",
                    e
            );
        }
    }

    @Override
    @Transactional
    public PaymentResponse markPaymentFailed(Long paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found"));

        if (payment.getPaymentStatus() == PaymentStatus.SUCCESS) {
            throw new RuntimeException(
                    "Successful payment cannot be marked as failed"
            );
        }

        if (payment.getPaymentStatus() == PaymentStatus.REFUNDED) {
            throw new RuntimeException(
                    "Refunded payment cannot be marked as failed"
            );
        }

        payment.setPaymentStatus(PaymentStatus.FAILED);

        Payment savedPayment =
                paymentRepository.save(payment);

        return paymentMapper.toResponse(savedPayment);
    }

    @Override
    @Transactional
    public void processPaymentCaptured(
            String razorpayOrderId,
            String razorpayPaymentId) {

        Payment payment = paymentRepository
                .findByRazorpayOrderIdForUpdate(razorpayOrderId)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found"));

        if (payment.getPaymentStatus() == PaymentStatus.SUCCESS) {
            return;
        }

        if (payment.getPaymentStatus() != PaymentStatus.CREATED
                && payment.getPaymentStatus() != PaymentStatus.PROCESSING) {

            throw new RuntimeException(
                    "Payment cannot be captured in current state"
            );
        }

        payment.setRazorpayPaymentId(razorpayPaymentId);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setPaidAt(LocalDateTime.now());

        Payment savedPayment =
                paymentRepository.save(payment);

        NotificationDetails notificationDetails =
                bookingServiceClient.getNotificationDetails(
                        savedPayment.getBookingId()
                );

        PaymentSuccessEvent event = PaymentSuccessEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .paymentId(savedPayment.getId())
                .bookingId(savedPayment.getBookingId())
                .userId(notificationDetails.getUserId())
                .email(notificationDetails.getEmail())
                .amount(savedPayment.getAmount())
                .razorpayOrderId(savedPayment.getRazorpayOrderId())
                .razorpayPaymentId(savedPayment.getRazorpayPaymentId())
                .paidAt(savedPayment.getPaidAt())
                .build();

        try {
            String payload = objectMapper.writeValueAsString(event);

            OutboxEvent outboxEvent = OutboxEvent.builder()
                    .eventId(event.getEventId())
                    .eventType(PaymentEventType.PAYMENT_SUCCESS)
                    .bookingId(event.getBookingId())
                    .payload(payload)
                    .published(false)
                    .createdAt(LocalDateTime.now())
                    .build();

            outboxEventRepository.save(outboxEvent);

        } catch (JacksonException e) {
            throw new RuntimeException(
                    "Failed to create payment event", e);
        }
    }

    @Override
    @Transactional
    public PaymentResponse refundPayment(Long paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found"));

        if (payment.getPaymentStatus() == PaymentStatus.REFUNDED) {
            return paymentMapper.toResponse(payment);
        }

        if (payment.getPaymentStatus() != PaymentStatus.SUCCESS) {
            throw new RuntimeException(
                    "Only successful payments can be refunded");
        }

        if (payment.getRazorpayPaymentId() == null) {
            throw new RuntimeException(
                    "Razorpay payment ID not found");
        }

        try {

            JSONObject options = new JSONObject();

            options.put(
                    "amount",
                    payment.getAmount()
                            .multiply(BigDecimal.valueOf(100))
                            .longValue()
            );

            Refund refund =
                    razorpayClient.payments.refund(
                            payment.getRazorpayPaymentId(),
                            options
                    );

            payment.setPaymentStatus(PaymentStatus.REFUNDED);
            payment.setRefundedAt(LocalDateTime.now());

            Payment savedPayment =
                    paymentRepository.save(payment);

            NotificationDetails notificationDetails =
                    bookingServiceClient.getNotificationDetails(
                            savedPayment.getBookingId()
                    );

            PaymentLateRefundedEvent event =
                    PaymentLateRefundedEvent.builder()
                            .eventId(UUID.randomUUID().toString())
                            .paymentId(savedPayment.getId())
                            .bookingId(savedPayment.getBookingId())
                            .userId(notificationDetails.getUserId())
                            .email(notificationDetails.getEmail())
                            .amount(savedPayment.getAmount())
                            .eventType(PaymentEventType.PAYMENT_LATE_REFUNDED)
                            .razorpayPaymentId(
                                    savedPayment.getRazorpayPaymentId()
                            )
                            .refundedAt(savedPayment.getRefundedAt())
                            .reason(
                                    "Booking expired before payment was completed"
                            )
                            .build();

            String payload =
                    objectMapper.writeValueAsString(event);

            OutboxEvent outboxEvent =
                    OutboxEvent.builder()
                            .eventId(event.getEventId())
                            .eventType(
                                    PaymentEventType.PAYMENT_LATE_REFUNDED
                            )
                            .bookingId(event.getBookingId())
                            .payload(payload)
                            .published(false)
                            .createdAt(LocalDateTime.now())
                            .build();

            outboxEventRepository.save(outboxEvent);

            return paymentMapper.toResponse(savedPayment);

        } catch (RazorpayException e) {

            throw new RuntimeException(
                    "Failed to refund Razorpay payment",
                    e
            );

        } catch (JacksonException e) {

            throw new RuntimeException(
                    "Failed to create refund notification event",
                    e
            );
        }
    }
}
