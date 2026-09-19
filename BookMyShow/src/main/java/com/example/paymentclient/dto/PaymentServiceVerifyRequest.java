package com.example.paymentclient.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentServiceVerifyRequest {

    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;
}
