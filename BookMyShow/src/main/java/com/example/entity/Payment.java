//package com.example.entity;
//
//import com.example.enums.PaymentStatus;
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "payment")
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class Payment {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "booking_id",nullable = false)
//    private Booking booking;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private PaymentStatus paymentStatus;
//
//    @Column(nullable = false, precision = 12, scale = 2)
//    private BigDecimal amount;
//
//    private LocalDateTime paidAt;
//
//    private LocalDateTime refundedAt;
//
//
//    @Column(nullable = false, unique = true, length = 100)
//    private String razorpayOrderId;
//
//    @Column(unique = true, length = 100)
//    private String razorpayPaymentId;
//}
