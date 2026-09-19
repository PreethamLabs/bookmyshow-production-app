package com.example.controller;

import com.example.dto.request.ScreenCreateRequest;
import com.example.dto.request.ScreenUpdateRequest;
import com.example.dto.request.response.ScreenResponse;
import com.example.service.ScreenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/screens")
@RequiredArgsConstructor
public class ScreenController {

    private final ScreenService screenService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ScreenResponse createScreen(
            @RequestBody ScreenCreateRequest request) {
        return screenService.createScreen(request);
    }

    @GetMapping("/{id}")
    public ScreenResponse getScreenById(
            @PathVariable Long id) {
        return screenService.getScreenById(id);
    }

    @GetMapping
    public List<ScreenResponse> getAllScreens() {
        return screenService.getAllScreens();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ScreenResponse updateScreen(
            @PathVariable Long id,
            @RequestBody ScreenUpdateRequest request) {
        return screenService.updateScreen(id, request);
    }
}
