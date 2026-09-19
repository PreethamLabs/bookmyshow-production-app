package com.example.service;

import com.example.dto.request.response.ShowSeatResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ShowSeatService {

    ShowSeatResponse getShowSeatById(Long id);

    List<ShowSeatResponse> getShowSeatsByShow(Long showId);

    ShowSeatResponse lockSeat(Long showSeatId);

    ShowSeatResponse releaseSeat(Long showSeatId);

    ShowSeatResponse bookSeat(Long showSeatId);
}
