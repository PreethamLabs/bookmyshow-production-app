package com.example.service;

import com.example.dto.request.LanguageCreateRequest;
import com.example.dto.request.response.LanguageResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface LanguageService {

    LanguageResponse createLanguage(LanguageCreateRequest request);

    LanguageResponse getLanguageById(Long id);

    List<LanguageResponse> getAllLanguages();
}

