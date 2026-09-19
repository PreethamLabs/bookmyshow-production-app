//package com.example.service.impl;
//
//import com.example.dto.request.PaymentCreateRequest;
//import com.example.dto.request.response.PaymentResponse;
//import com.example.entity.Booking;
//import com.example.entity.BookingSeat;
//import com.example.entity.Payment;
//import com.example.entity.ShowSeat;
//import com.example.enums.BookingStatus;
//import com.example.enums.PaymentStatus;
//import com.example.enums.SeatStatus;
//import com.example.exception.PaymentAlreadyExistsException;
//import com.example.mapper.PaymentMapper;
//import com.example.repository.BookingRepository;
//import com.example.repository.BookingSeatRepository;
//import com.example.repository.PaymentRepository;
//import com.example.repository.ShowSeatRepository;
//import com.example.service.PaymentService;
//import com.example.service.SeatLockService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class PaymentServiceImpl implements PaymentService {
//
//    private final PaymentRepository paymentRepository;
//    private final BookingRepository bookingRepository;
//    private final BookingSeatRepository bookingSeatRepository;
//    private final ShowSeatRepository showSeatRepository;
//    private final SeatLockService seatLockService;
//    private final PaymentMapper paymentMapper;
//
//    @Override
//    public PaymentResponse createPayment(
//            String email,
//            PaymentCreateRequest request) {
//
//        Booking booking = bookingRepository.findById(request.getBookingId())
//                .orElseThrow(() -> new RuntimeException("Booking not found"));
//
//        if (!booking.getUser().getEmail().equals(email)) {
//            throw new AccessDeniedException("You cannot create payment for this booking");
//        }
//
//        if (booking.getBookingStatus() != BookingStatus.PENDING_PAYMENT) {
//            throw new RuntimeException("Booking is not awaiting payment");
//        }
//        Optional<Payment> existingPayment =
//                paymentRepository.findByBookingIdAndPaymentStatus(
//                        booking.getId(),
//                        PaymentStatus.CREATED
//                );
//
//        if (existingPayment.isPresent()) {
//            return paymentMapper.toResponse(existingPayment.get());
//        }
//
//        Payment payment = Payment.builder()
//                .booking(booking)
//                .paymentStatus(PaymentStatus.CREATED)
//                .amount(booking.getTotalPrice())
//                .build();
//
//        try {
//            Payment savedPayment = paymentRepository.save(payment);
//
//            return paymentMapper.toResponse(savedPayment);
//
//        } catch (
//                DataIntegrityViolationException e) {
//            throw new PaymentAlreadyExistsException(
//                    "An active payment already exists for this booking"
//            );
//        }
//    }
//
//    @Override
//    public PaymentResponse getPaymentById(Long id) {
//
//        Payment payment = paymentRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Payment not found"));
//
//        return paymentMapper.toResponse(payment);
//    }
//
//    @Override
//    public List<PaymentResponse> getPaymentsByBooking(Long bookingId) {
//
//        return paymentRepository.findPaymentByBookingId(bookingId)
//                .stream()
//                .map(paymentMapper::toResponse)
//                .toList();
//    }
//
//    @Transactional
//    @Override
//    public void markPaymentSuccessful(Long paymentId) {
//
//        Payment payment = paymentRepository.findById(paymentId)
//                .orElseThrow(() -> new RuntimeException("Payment not found"));
//
//        if (payment.getPaymentStatus() == PaymentStatus.SUCCESS) {
//            return;
//        }
//
//        Booking booking = payment.getBooking();
//
//        if (booking.getBookingStatus() != BookingStatus.PENDING_PAYMENT) {
//            throw new RuntimeException("Booking is not awaiting payment");
//        }
//
//        List<BookingSeat> bookingSeats =
//                bookingSeatRepository.findByBookingId(booking.getId());
//
//        for (BookingSeat bookingSeat : bookingSeats) {
//
//            ShowSeat showSeat = bookingSeat.getShowSeat();
//
//            showSeat.setSeatStatus(SeatStatus.BOOKED);
//
//            showSeatRepository.save(showSeat);
//        }
//
//        payment.setPaymentStatus(PaymentStatus.SUCCESS);
//        payment.setPaidAt(LocalDateTime.now());
//
//        booking.setBookingStatus(BookingStatus.CONFIRMED);
//        booking.setBookedAt(LocalDateTime.now());
//
//        paymentRepository.save(payment);
//        bookingRepository.save(booking);
//
//        List<Long> showSeatIds = bookingSeats.stream()
//                .map(bookingSeat -> bookingSeat.getShowSeat().getId())
//                .toList();
//
//        seatLockService.releaseSeats(
//                booking.getShow().getId(),
//                showSeatIds,
//                booking.getLockToken()
//        );
//    }
//}
