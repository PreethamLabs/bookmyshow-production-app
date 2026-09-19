package com.example.dto.request.response;

import com.example.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {

    private Long id;

    private Long userId;

    private Long showId;

    private BookingStatus bookingStatus;

    private BigDecimal totalPrice;

    private LocalDateTime createdAt;

    private LocalDateTime bookedAt;

    private LocalDateTime cancelledAt;
}
