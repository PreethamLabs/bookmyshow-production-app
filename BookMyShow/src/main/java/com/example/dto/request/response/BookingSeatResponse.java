package com.example.dto.request.response;

import com.example.enums.SeatType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingSeatResponse {

    private Long id;

    private Long bookingId;

    private Long showSeatId;

    private String seatNumber;

    private SeatType seatType;

    private BigDecimal price;
}
