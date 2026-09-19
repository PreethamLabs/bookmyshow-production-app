package com.example.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "movie_genre")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieGenre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id",nullable = false)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "genre_id",nullable = false)
    private Genre genre;
}
