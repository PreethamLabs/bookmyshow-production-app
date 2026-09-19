package com.example.service;

import java.util.List;

public interface SeatLockService {

    String lockSeats(Long showId, List<Long> showSeatIds);

    void releaseSeats(Long showId, List<Long> showSeatIds, String lockToken);
}
