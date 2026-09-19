package com.example.service;

import com.example.dto.request.SeatCreateRequest;
import com.example.dto.request.SeatUpdateRequest;
import com.example.dto.request.response.SeatResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface SeatService {

        SeatResponse createSeat(SeatCreateRequest request);

        SeatResponse getSeatById(Long id);

        List<SeatResponse> getAllSeats();

        SeatResponse updateSeat(Long id, SeatUpdateRequest request);

}
