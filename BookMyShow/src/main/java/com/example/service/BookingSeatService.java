package com.example.service;

import com.example.dto.request.response.BookingSeatResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookingSeatService {
    BookingSeatResponse getBookingSeatById(Long id);

    List<BookingSeatResponse> getBookingSeatsByBooking(Long bookingId);
}
