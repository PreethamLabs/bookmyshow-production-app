package com.example.paymentservice.controller;

import com.example.paymentservice.service.PaymentService;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhooks/razorpay")
@RequiredArgsConstructor
public class RazorpayWebhookController {

    private final PaymentService paymentService;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    @PostMapping
    public ResponseEntity<Void> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("X-Razorpay-Signature") String signature) {

        try {

            // Step 1 — Verify Razorpay webhook signature
            boolean valid = Utils.verifyWebhookSignature(
                    payload,
                    signature,
                    razorpayKeySecret
            );

            if (!valid) {
                return ResponseEntity.status(401).build();
            }

            // Step 2 — Parse webhook payload
            JSONObject webhook = new JSONObject(payload);

            String event = webhook.getString("event");

            // Step 3 — Handle successful payment
            if ("payment.captured".equals(event)) {

                JSONObject paymentEntity =
                        webhook
                                .getJSONObject("payload")
                                .getJSONObject("payment")
                                .getJSONObject("entity");

                String razorpayPaymentId =
                        paymentEntity.getString("id");

                String razorpayOrderId =
                        paymentEntity.getString("order_id");

                // Processing will be added next.
                paymentService.processPaymentCaptured(
                        razorpayOrderId,
                        razorpayPaymentId
                );
            }

            return ResponseEntity.ok().build();

        } catch (RazorpayException e) {

            return ResponseEntity.status(401).build();

        } catch (Exception e) {

            return ResponseEntity.badRequest().build();
        }
    }
}
