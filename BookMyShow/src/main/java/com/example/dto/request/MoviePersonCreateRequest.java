package com.example.dto.request;

import com.example.enums.MovieRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MoviePersonCreateRequest {

    private Long movieId;

    private Long personId;

    private MovieRole role;
}
