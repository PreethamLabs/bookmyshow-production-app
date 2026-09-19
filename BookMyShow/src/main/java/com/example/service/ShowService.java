package com.example.service;

import com.example.dto.request.ShowCreateRequest;
import com.example.dto.request.ShowUpdateRequest;
import com.example.dto.request.response.ShowResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ShowService {

    ShowResponse createShow(ShowCreateRequest request);

    ShowResponse getShowById(Long id);

    List<ShowResponse> getAllShows();

    ShowResponse updateShow(Long id, ShowUpdateRequest request);

    void deleteShow(Long id);
}
