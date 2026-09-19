package com.example.entity;

import com.example.enums.MovieRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "movie_person")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MoviePerson {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovieRole role;
}
