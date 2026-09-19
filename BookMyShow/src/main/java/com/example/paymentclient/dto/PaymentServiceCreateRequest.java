package com.example.paymentclient.dto;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentServiceCreateRequest {

    private Long bookingId;
    private BigDecimal amount;
}
