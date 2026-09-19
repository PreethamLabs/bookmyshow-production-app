package com.example.service;

import com.example.dto.request.TheatreCreateRequest;
import com.example.dto.request.TheatreUpdateRequest;
import com.example.dto.request.response.TheatreResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface TheatreService {

    TheatreResponse createTheatre(TheatreCreateRequest request);

    TheatreResponse getTheatreById(Long id);

    List<TheatreResponse> getAllTheatres();

    TheatreResponse updateTheatre(Long id, TheatreUpdateRequest request);
}
