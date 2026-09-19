package com.example.controller;

import com.example.dto.request.ShowCreateRequest;
import com.example.dto.request.ShowUpdateRequest;
import com.example.dto.request.response.ShowResponse;
import com.example.service.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/shows")
@RequiredArgsConstructor
public class ShowController {

    private final ShowService showService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ShowResponse createShow(
            @RequestBody ShowCreateRequest request) {
        return showService.createShow(request);
    }

    @GetMapping("/{id}")
    public ShowResponse getShowById(
            @PathVariable Long id) {
        return showService.getShowById(id);
    }

    @GetMapping
    public List<ShowResponse> getAllShows() {
        return showService.getAllShows();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ShowResponse updateShow(
            @PathVariable Long id,
            @RequestBody ShowUpdateRequest request) {
        return showService.updateShow(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShow(
            @PathVariable Long id) {

        showService.deleteShow(id);

        return ResponseEntity.noContent().build();
    }
}
