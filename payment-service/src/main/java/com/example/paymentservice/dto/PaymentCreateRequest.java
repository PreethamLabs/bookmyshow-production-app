package com.example.paymentservice.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PaymentCreateRequest {

    @NotNull
    private Long bookingId;

}
