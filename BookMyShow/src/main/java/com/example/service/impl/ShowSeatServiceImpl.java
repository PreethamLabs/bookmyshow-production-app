package com.example.service.impl;

import com.example.dto.request.response.ShowSeatResponse;
import com.example.entity.ShowSeat;
import com.example.mapper.ShowSeatMapper;
import com.example.repository.ShowSeatRepository;
import com.example.service.ShowSeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowSeatServiceImpl implements ShowSeatService {

    private final ShowSeatRepository showSeatRepository;
    private final ShowSeatMapper showSeatMapper;

    @Override
    public ShowSeatResponse getShowSeatById(Long id) {

        ShowSeat showSeat = showSeatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Show seat not found"));

        return showSeatMapper.toResponse(showSeat);
    }

    @Override
    public List<ShowSeatResponse> getShowSeatsByShow(Long showId) {

        return showSeatRepository.findByShowId(showId)
                .stream()
                .map(showSeatMapper::toResponse)
                .toList();
    }

    @Override
    public ShowSeatResponse lockSeat(Long showSeatId) {

        ShowSeat showSeat = showSeatRepository.findById(showSeatId)
                .orElseThrow(() -> new RuntimeException("Show seat not found"));

        return showSeatMapper.toResponse(showSeat);
    }

    @Override
    public ShowSeatResponse releaseSeat(Long showSeatId) {

        ShowSeat showSeat = showSeatRepository.findById(showSeatId)
                .orElseThrow(() -> new RuntimeException("Show seat not found"));

        return showSeatMapper.toResponse(showSeat);
    }

    @Override
    public ShowSeatResponse bookSeat(Long showSeatId) {

        ShowSeat showSeat = showSeatRepository.findById(showSeatId)
                .orElseThrow(() -> new RuntimeException("Show seat not found"));

        return showSeatMapper.toResponse(showSeat);
    }
}
