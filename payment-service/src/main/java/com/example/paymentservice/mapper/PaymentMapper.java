package com.example.paymentservice.mapper;

import com.example.paymentservice.dto.PaymentResponse;
import com.example.paymentservice.entity.Payment;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentResponse toResponse(Payment payment);
}
