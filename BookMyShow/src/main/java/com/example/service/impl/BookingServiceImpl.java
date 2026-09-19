package com.example.service.impl;

import com.example.dto.request.BookingCreateRequest;
import com.example.dto.request.NotificationDetails;
import com.example.dto.request.response.BookingResponse;
import com.example.entity.*;
import com.example.enums.BookingStatus;
import com.example.enums.SeatStatus;
import com.example.mapper.BookingMapper;
import com.example.paymentclient.PaymentServiceClient;
import com.example.paymentclient.dto.PaymentServiceCreateRequest;
import com.example.paymentclient.dto.PaymentServiceResponse;
import com.example.repository.*;
import com.example.service.BookingService;
import com.example.service.SeatLockService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ShowRepository showRepository;
    private final BookingMapper bookingMapper;
    private final ShowSeatRepository showSeatRepository;
    private final SeatLockService seatLockService;
    private final BookingSeatRepository bookingSeatRepository;
    private final PaymentServiceClient paymentServiceClient;

    @Transactional
    @Override
    public BookingResponse createBooking(
            String email,
            BookingCreateRequest request) {

        String lockToken = seatLockService.lockSeats(
                request.getShowId(),
                request.getShowSeatIds()
        );



        if (lockToken == null) {
            throw new RuntimeException("One or more seats are currently locked");
        }

        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Show show = showRepository.findById(request.getShowId())
                    .orElseThrow(() -> new RuntimeException("Show not found"));

            // Step 3 — fetch seats
            List<ShowSeat> showSeats =
                    showSeatRepository.findAllByIdsForUpdate(request.getShowSeatIds());

            // Step 3 — verify all seats exist
            if (showSeats.size() != request.getShowSeatIds().size()) {
                throw new RuntimeException("One or more seats not found");
            }

            // Step 4 — verify seats belong to this show
            boolean invalidShowSeat = showSeats.stream()
                    .anyMatch(showSeat ->
                            !showSeat.getShow().getId().equals(request.getShowId()));

            if (invalidShowSeat) {
                throw new RuntimeException("One or more seats do not belong to this show");
            }

            // Step 5 — verify seats are available
            boolean unavailableSeat = showSeats.stream()
                    .anyMatch(showSeat ->
                            showSeat.getSeatStatus() != SeatStatus.AVAILABLE);

            if (unavailableSeat) {
                throw new RuntimeException("One or more seats are not available");
            }

            // Step 6 — calculate price
            BigDecimal totalPrice = showSeats.stream()
                    .map(showSeat -> show.getTicketPrice())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Booking booking = Booking.builder()
                    .user(user)
                    .show(show)
                    .bookingStatus(BookingStatus.PENDING_PAYMENT)
                    .totalPrice(totalPrice)
                    .lockToken(lockToken)
                    .build();

            Booking savedBooking = bookingRepository.save(booking);

            List<BookingSeat> bookingSeats = showSeats.stream()
                    .map(showSeat -> BookingSeat.builder()
                            .booking(savedBooking)
                            .showSeat(showSeat)
                            .price(show.getTicketPrice())
                            .build())
                    .toList();

            bookingSeatRepository.saveAll(bookingSeats);

            return bookingMapper.toResponse(savedBooking);
        } catch (Exception e) {
            seatLockService.releaseSeats(
                    request.getShowId(),
                    request.getShowSeatIds(),
                    lockToken);
            throw e;

        }
    }

    @Override
    public BookingResponse getBookingById(Long id, String email) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException("You cannot access this booking");
        }

        return bookingMapper.toResponse(booking);
    }

    @Override
    public List<BookingResponse> getBookingsByUserEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        return bookingRepository.findBookingsByUserId(user.getId())
                .stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Override
    public BookingResponse cancelBooking(Long id, String email) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found")
                );

        if (!booking.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException(
                    "You cannot cancel this booking"
            );
        }

        booking.setBookingStatus(BookingStatus.CANCELLED);
        booking.setCancelledAt(LocalDateTime.now());

        Booking savedBooking = bookingRepository.save(booking);

        return bookingMapper.toResponse(savedBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getBookingAmount(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found"));

        return booking.getTotalPrice();
    }

    @Override
    @Transactional
    public PaymentServiceResponse createPayment(Long bookingId, String email) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found"));

        if (!booking.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException("You do not own this booking");
        }

        if (booking.getBookingStatus() != BookingStatus.PENDING_PAYMENT) {
            throw new RuntimeException(
                    "Payment cannot be created for this booking");
        }

        PaymentServiceCreateRequest request =
                new PaymentServiceCreateRequest();

        request.setBookingId(bookingId);

        return paymentServiceClient.createPayment(request);
    }

    @Override
    @Transactional
    public void markPaymentSuccessful(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found"));

        if (booking.getBookingStatus() == BookingStatus.CONFIRMED) {
            return;
        }

        if (booking.getBookingStatus() != BookingStatus.PENDING_PAYMENT) {
            throw new RuntimeException(
                    "Booking cannot be confirmed in current state");
        }

        List<BookingSeat> bookingSeats =
                bookingSeatRepository.findByBookingId(bookingId);

        for (BookingSeat bookingSeat : bookingSeats) {
            ShowSeat showSeat = bookingSeat.getShowSeat();
            showSeat.setSeatStatus(SeatStatus.BOOKED);
        }

        booking.setBookingStatus(BookingStatus.CONFIRMED);
        booking.setBookedAt(LocalDateTime.now());

        bookingRepository.save(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationDetails getNotificationDetails(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found"));

        User user = booking.getUser();

        return NotificationDetails.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public BookingStatus getBookingStatus(Long bookingId) {

        return bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found"))
                .getBookingStatus();
    }
}
