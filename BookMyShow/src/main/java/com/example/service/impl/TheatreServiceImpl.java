package com.example.service.impl;

import com.example.dto.request.TheatreCreateRequest;
import com.example.dto.request.TheatreUpdateRequest;
import com.example.dto.request.response.TheatreResponse;
import com.example.entity.City;
import com.example.entity.Theatre;
import com.example.mapper.TheatreMapper;
import com.example.repository.CityRepository;
import com.example.repository.TheatreRepository;
import com.example.service.TheatreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class TheatreServiceImpl implements TheatreService {

    private final TheatreRepository theatreRepository;
    private final CityRepository cityRepository;
    private final TheatreMapper theatreMapper;

    @Override
    public TheatreResponse createTheatre(TheatreCreateRequest request) {

        Theatre theatre = theatreMapper.toEntity(request);

        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new RuntimeException("City not found"));

        theatre.setCity(city);

        return theatreMapper.toResponse(
                theatreRepository.save(theatre)
        );
    }

    @Override
    public TheatreResponse getTheatreById(Long id) {

        Theatre theatre = theatreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Theatre not found"));

        return theatreMapper.toResponse(theatre);
    }

    @Override
    public List<TheatreResponse> getAllTheatres() {

        return theatreRepository.findAll()
                .stream()
                .map(theatreMapper::toResponse)
                .toList();
    }

    @Override
    public TheatreResponse updateTheatre(Long id, TheatreUpdateRequest request) {

        Theatre theatre = theatreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Theatre not found"));

        theatreMapper.updateEntity(request, theatre);

        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new RuntimeException("City not found"));

        theatre.setCity(city);

        return theatreMapper.toResponse(
                theatreRepository.save(theatre)
        );
    }
}
