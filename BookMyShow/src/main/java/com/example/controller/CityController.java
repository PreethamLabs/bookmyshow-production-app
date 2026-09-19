package com.example.controller;

import com.example.dto.request.CityCreateRequest;
import com.example.dto.request.response.CityResponse;
import com.example.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CityResponse> createCity(
            @RequestBody CityCreateRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cityService.createCity(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityResponse> getCityById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                cityService.getCityById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<CityResponse>> getAllCities() {

        return ResponseEntity.ok(
                cityService.getAllCities()
        );
    }
}
