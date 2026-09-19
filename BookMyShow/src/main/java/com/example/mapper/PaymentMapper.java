//package com.example.mapper;
//
//import com.example.dto.request.PaymentCreateRequest;
//import com.example.dto.request.response.PaymentResponse;
//import com.example.entity.Payment;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//
//@Mapper(componentModel = "spring")
//public interface PaymentMapper {
//
//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "booking", ignore = true)
//    @Mapping(target = "paymentStatus", ignore = true)
//    @Mapping(target = "amount", ignore = true)
//    @Mapping(target = "paidAt", ignore = true)
//    @Mapping(target = "refundedAt", ignore = true)
//    @Mapping(target = "razorpayOrderId", ignore = true)
//    @Mapping(target = "razorpayPaymentId", ignore = true)
//    Payment toEntity(PaymentCreateRequest request);
//
//    @Mapping(target = "bookingId", source = "booking.id")
//    PaymentResponse toResponse(Payment payment);
//}
