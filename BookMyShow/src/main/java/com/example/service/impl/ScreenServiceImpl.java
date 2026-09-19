package com.example.service.impl;

import com.example.dto.request.ScreenCreateRequest;
import com.example.dto.request.ScreenUpdateRequest;
import com.example.dto.request.response.ScreenResponse;
import com.example.entity.Screen;
import com.example.entity.Theatre;
import com.example.mapper.ScreenMapper;
import com.example.repository.ScreenRepository;
import com.example.repository.TheatreRepository;
import com.example.service.ScreenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScreenServiceImpl implements ScreenService {

    private final ScreenRepository screenRepository;
    private final TheatreRepository theatreRepository;
    private final ScreenMapper screenMapper;

    @Override
    public ScreenResponse createScreen(ScreenCreateRequest request) {

        Screen screen = screenMapper.toEntity(request);

        Theatre theatre = theatreRepository.findById(request.getTheatreId())
                .orElseThrow(() -> new RuntimeException("Theatre not found"));

        screen.setTheatre(theatre);

        Screen savedScreen = screenRepository.save(screen);

        return screenMapper.toResponse(savedScreen);
    }

    @Override
    public ScreenResponse getScreenById(Long id) {

        Screen screen = screenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Screen not found"));

        return screenMapper.toResponse(screen);
    }

    @Override
    public List<ScreenResponse> getAllScreens() {

        return screenRepository.findAll()
                .stream()
                .map(screenMapper::toResponse)
                .toList();
    }

    @Override
    public ScreenResponse updateScreen(
            Long id,
            ScreenUpdateRequest request) {

        Screen screen = screenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Screen not found"));

        screenMapper.updateEntity(request, screen);

        Theatre theatre = theatreRepository.findById(request.getTheatreId())
                .orElseThrow(() -> new RuntimeException("Theatre not found"));

        screen.setTheatre(theatre);

        Screen updatedScreen = screenRepository.save(screen);

        return screenMapper.toResponse(updatedScreen);
    }
}
