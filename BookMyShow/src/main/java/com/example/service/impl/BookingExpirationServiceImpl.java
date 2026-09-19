package com.example.service.impl;

import com.example.entity.Booking;
import com.example.entity.BookingSeat;
import com.example.enums.BookingStatus;
import com.example.repository.BookingRepository;
import com.example.repository.BookingSeatRepository;
import com.example.service.BookingExpirationService;
import com.example.service.SeatLockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingExpirationServiceImpl implements BookingExpirationService {

    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final SeatLockService seatLockService;

    @Override
    @Transactional
    public void expireBooking(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getBookingStatus() != BookingStatus.PENDING_PAYMENT) {
            return;
        }

        List<BookingSeat> bookingSeats =
                bookingSeatRepository.findByBookingId(bookingId);

        List<Long> showSeatIds = bookingSeats.stream()
                .map(bookingSeat -> bookingSeat.getShowSeat().getId())
                .toList();

        seatLockService.releaseSeats(
                booking.getShow().getId(),
                showSeatIds,
                booking.getLockToken()
        );

        booking.setBookingStatus(BookingStatus.EXPIRED);
        bookingRepository.save(booking);
    }

}
