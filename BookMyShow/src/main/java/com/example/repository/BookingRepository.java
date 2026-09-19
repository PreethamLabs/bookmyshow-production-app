package com.example.repository;

import com.example.entity.Booking;
import com.example.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {
    List<Booking> findBookingsByUserId(Long userId);

    List<Booking> findByBookingStatus(BookingStatus bookingStatus);
}
