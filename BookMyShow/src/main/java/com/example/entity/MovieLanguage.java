package com.example.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "movie_language")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieLanguage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id",nullable = false)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "language_id",nullable = false)
    private Language language;
}
