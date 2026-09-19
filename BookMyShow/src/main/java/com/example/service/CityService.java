package com.example.service;

import com.example.dto.request.CityCreateRequest;
import com.example.dto.request.response.CityResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CityService {
    CityResponse createCity(CityCreateRequest request);

    CityResponse getCityById(Long id);

    List<CityResponse> getAllCities();
}
