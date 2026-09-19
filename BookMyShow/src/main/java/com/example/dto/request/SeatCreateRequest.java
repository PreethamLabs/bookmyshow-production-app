package com.example.dto.request;

import com.example.enums.SeatType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeatCreateRequest {

    private String seatNumber;

    private SeatType seatType;

    private Long screenId;
}
