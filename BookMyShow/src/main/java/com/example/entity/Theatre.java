package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "theatre")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Theatre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, length = 300)
    private String address;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;
}
