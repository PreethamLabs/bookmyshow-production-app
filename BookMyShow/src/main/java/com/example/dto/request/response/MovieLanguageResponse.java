package com.example.dto.request.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieLanguageResponse {

    private Long id;

    private Long movieId;

    private Long languageId;
}
