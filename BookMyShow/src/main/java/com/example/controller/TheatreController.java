package com.example.controller;

import com.example.dto.request.TheatreCreateRequest;
import com.example.dto.request.TheatreUpdateRequest;
import com.example.dto.request.response.TheatreResponse;
import com.example.service.TheatreService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theatres")
@RequiredArgsConstructor
public class TheatreController {

    private final TheatreService theatreService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public TheatreResponse createTheatre(
            @RequestBody TheatreCreateRequest request) {
        return theatreService.createTheatre(request);
    }

    @GetMapping("/{id}")
    public TheatreResponse getTheatreById(
            @PathVariable Long id) {
        return theatreService.getTheatreById(id);
    }

    @GetMapping
    public List<TheatreResponse> getAllTheatres() {
        return theatreService.getAllTheatres();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public TheatreResponse updateTheatre(
            @PathVariable Long id,
            @RequestBody TheatreUpdateRequest request) {
        return theatreService.updateTheatre(id, request);
    }
}
