package com.example.dto.request.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TheatreResponse {

    private Long id;

    private String name;

    private String address;

    private Long cityId;
}
