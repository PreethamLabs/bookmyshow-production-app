package com.example.dto.request.response;

import com.example.enums.SeatStatus;
import com.example.enums.SeatType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShowSeatResponse {

    private Long id;

    private Long showId;

    private Long seatId;

    private String seatNumber;

    private SeatType seatType;

    private SeatStatus seatStatus;

    private LocalDateTime lockedUntil;
}
