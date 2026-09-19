package com.example.service.impl;

import com.example.dto.request.response.BookingSeatResponse;
import com.example.entity.BookingSeat;
import com.example.mapper.BookingSeatMapper;
import com.example.repository.BookingSeatRepository;
import com.example.service.BookingSeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingSeatServiceImpl implements BookingSeatService {

    private final BookingSeatRepository bookingSeatRepository;
    private final BookingSeatMapper bookingSeatMapper;

    @Override
    public BookingSeatResponse getBookingSeatById(Long id) {

        BookingSeat bookingSeat = bookingSeatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking seat not found"));

        return bookingSeatMapper.toResponse(bookingSeat);
    }

    @Override
    public List<BookingSeatResponse> getBookingSeatsByBooking(Long bookingId) {

        return bookingSeatRepository.findByBookingId(bookingId)
                .stream()
                .map(bookingSeatMapper::toResponse)
                .toList();
    }
}
