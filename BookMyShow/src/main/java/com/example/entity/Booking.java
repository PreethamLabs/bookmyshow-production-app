package com.example.entity;

import com.example.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "show_id",nullable = false)
    private Show show;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus bookingStatus;

    @Column(nullable = false,precision = 12, scale = 2)
    private BigDecimal totalPrice;

    @Column(nullable = false, unique = true, length = 100)
    private String lockToken;

    private LocalDateTime bookedAt;

    private LocalDateTime createdAt;

    private LocalDateTime cancelledAt;

    @PrePersist
    protected void OnCreate(){
        this.createdAt = LocalDateTime.now();
    }

}
