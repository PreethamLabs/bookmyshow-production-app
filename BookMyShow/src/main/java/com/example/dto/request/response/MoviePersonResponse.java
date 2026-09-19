package com.example.dto.request.response;


import com.example.enums.MovieRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MoviePersonResponse {

    private Long id;

    private Long movieId;

    private Long personId;

    private MovieRole role;
}
