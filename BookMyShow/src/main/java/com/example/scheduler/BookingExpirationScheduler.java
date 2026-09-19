package com.example.scheduler;

import com.example.entity.Booking;
import com.example.enums.BookingStatus;
import com.example.repository.BookingRepository;
import com.example.service.BookingExpirationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingExpirationScheduler {

    private final BookingRepository bookingRepository;
    private final BookingExpirationService bookingExpirationService;

    @Scheduled(fixedRate = 30000)
    public void expirePendingBookings() {

        List<Booking> bookings =
                bookingRepository.findByBookingStatus(BookingStatus.PENDING_PAYMENT);

        for (Booking booking : bookings) {

            if (booking.getCreatedAt()
                    .plusMinutes(10)
                    .isBefore(LocalDateTime.now())) {

                bookingExpirationService.expireBooking(booking.getId());
            }
        }
    }
}
