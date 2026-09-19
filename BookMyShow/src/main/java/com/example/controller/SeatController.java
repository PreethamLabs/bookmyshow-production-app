package com.example.controller;

import com.example.dto.request.SeatCreateRequest;
import com.example.dto.request.SeatUpdateRequest;
import com.example.dto.request.response.SeatResponse;
import com.example.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public SeatResponse createSeat(
            @RequestBody SeatCreateRequest request) {
        return seatService.createSeat(request);
    }

    @GetMapping("/{id}")
    public SeatResponse getSeatById(
            @PathVariable Long id) {
        return seatService.getSeatById(id);
    }

    @GetMapping
    public List<SeatResponse> getAllSeats() {
        return seatService.getAllSeats();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public SeatResponse updateSeat(
            @PathVariable Long id,
            @RequestBody SeatUpdateRequest request) {
        return seatService.updateSeat(id, request);
    }
}
