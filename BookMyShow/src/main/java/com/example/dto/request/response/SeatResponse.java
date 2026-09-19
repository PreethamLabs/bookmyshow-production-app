package com.example.dto.request.response;

import com.example.enums.SeatType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeatResponse {

    private Long id;

    private String seatNumber;

    private SeatType seatType;

    private Long screenId;
}
