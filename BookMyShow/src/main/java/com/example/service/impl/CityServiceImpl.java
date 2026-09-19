package com.example.service.impl;

import com.example.dto.request.CityCreateRequest;
import com.example.dto.request.response.CityResponse;
import com.example.entity.City;
import com.example.mapper.CityMapper;
import com.example.repository.CityRepository;
import com.example.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    @Override
    public CityResponse createCity(CityCreateRequest request) {

        City city = cityMapper.toEntity(request);

        City savedCity = cityRepository.save(city);

        return cityMapper.toResponse(savedCity);
    }

    @Override
    public CityResponse getCityById(Long id) {

        City city = cityRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("City not found with id: " + id));

        return cityMapper.toResponse(city);
    }

    @Override
    public List<CityResponse> getAllCities() {

        return cityRepository.findAll()
                .stream()
                .map(cityMapper::toResponse)
                .toList();
    }
}
