package com.example.controller;

import com.example.dto.request.response.ShowSeatResponse;
import com.example.service.ShowSeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/show-seats")
@RequiredArgsConstructor
public class ShowSeatController {

    private final ShowSeatService showSeatService;

    @GetMapping("/{id}")
    public ShowSeatResponse getShowSeatById(
            @PathVariable Long id) {
        return showSeatService.getShowSeatById(id);
    }

    @GetMapping("/show/{showId}")
    public List<ShowSeatResponse> getShowSeatsByShow(
            @PathVariable Long showId) {
        return showSeatService.getShowSeatsByShow(showId);
    }

    @PutMapping("/{id}/lock")
    public ShowSeatResponse lockSeat(
            @PathVariable Long id) {
        return showSeatService.lockSeat(id);
    }

    @PutMapping("/{id}/release")
    public ShowSeatResponse releaseSeat(
            @PathVariable Long id) {
        return showSeatService.releaseSeat(id);
    }

    @PutMapping("/{id}/book")
    public ShowSeatResponse bookSeat(
            @PathVariable Long id) {
        return showSeatService.bookSeat(id);
    }
}
