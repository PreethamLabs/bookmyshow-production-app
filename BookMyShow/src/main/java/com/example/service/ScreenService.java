package com.example.service;

import com.example.dto.request.ScreenCreateRequest;
import com.example.dto.request.ScreenUpdateRequest;
import com.example.dto.request.response.ScreenResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ScreenService {

    ScreenResponse createScreen(ScreenCreateRequest request);

    ScreenResponse getScreenById(Long id);

    List<ScreenResponse> getAllScreens();

    ScreenResponse updateScreen(Long id, ScreenUpdateRequest request);
}
