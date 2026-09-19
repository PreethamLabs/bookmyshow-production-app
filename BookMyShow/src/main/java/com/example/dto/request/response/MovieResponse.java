package com.example.dto.request.response;

import com.example.enums.MovieStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MovieResponse {

    private Long id;
    private String title;
    private String description;
    private String posterUrl;
    private MovieStatus movieStatus;
    private Integer durationInMinutes;
    private LocalDate releaseDate;
}
