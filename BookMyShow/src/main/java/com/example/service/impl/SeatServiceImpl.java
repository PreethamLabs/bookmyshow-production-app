package com.example.service.impl;

import com.example.dto.request.SeatCreateRequest;
import com.example.dto.request.SeatUpdateRequest;
import com.example.dto.request.response.SeatResponse;
import com.example.entity.Screen;
import com.example.entity.Seat;
import com.example.mapper.SeatMapper;
import com.example.repository.ScreenRepository;
import com.example.repository.SeatRepository;
import com.example.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final ScreenRepository screenRepository;
    private final SeatMapper seatMapper;

    @Override
    public SeatResponse createSeat(SeatCreateRequest request) {

        Seat seat = seatMapper.toEntity(request);

        Screen screen = screenRepository.findById(request.getScreenId())
                .orElseThrow(() -> new RuntimeException("Screen not found"));

        seat.setScreen(screen);

        Seat savedSeat = seatRepository.save(seat);

        return seatMapper.toResponse(savedSeat);
    }

    @Override
    public SeatResponse getSeatById(Long id) {

        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        return seatMapper.toResponse(seat);
    }

    @Override
    public List<SeatResponse> getAllSeats() {

        return seatRepository.findAll()
                .stream()
                .map(seatMapper::toResponse)
                .toList();
    }

    @Override
    public SeatResponse updateSeat(
            Long id,
            SeatUpdateRequest request) {

        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        seatMapper.updateEntity(request, seat);

        Screen screen = screenRepository.findById(request.getScreenId())
                .orElseThrow(() -> new RuntimeException("Screen not found"));

        seat.setScreen(screen);

        Seat updatedSeat = seatRepository.save(seat);

        return seatMapper.toResponse(updatedSeat);
    }
}
