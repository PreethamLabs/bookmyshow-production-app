package com.example.dto.request.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShowResponse {

    private Long id;

    private Long movieId;

    private Long screenId;

    private LocalDate showDate;

    private BigDecimal ticketPrice;

    private LocalTime startTime;

    private LocalTime endTime;
}
