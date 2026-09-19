//package com.example.repository;
//
//import com.example.entity.Payment;
//import com.example.enums.PaymentStatus;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface PaymentRepository extends JpaRepository<Payment,Long> {
//
//    List<Payment> findPaymentByBookingId(Long bookingId);
//
//    Optional<Payment> findByBookingIdAndPaymentStatus(
//            Long bookingId,
//            PaymentStatus paymentStatus
//    );
//}
