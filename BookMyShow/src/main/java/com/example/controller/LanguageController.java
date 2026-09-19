package com.example.controller;

import com.example.dto.request.LanguageCreateRequest;
import com.example.dto.request.response.LanguageResponse;
import com.example.service.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/languages")
@RequiredArgsConstructor
public class LanguageController {

    private final LanguageService languageService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<LanguageResponse> createLanguage(
            @RequestBody LanguageCreateRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(languageService.createLanguage(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LanguageResponse> getLanguageById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                languageService.getLanguageById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<LanguageResponse>> getAllLanguages() {

        return ResponseEntity.ok(
                languageService.getAllLanguages()
        );
    }
}