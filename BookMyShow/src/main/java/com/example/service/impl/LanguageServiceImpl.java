package com.example.service.impl;

import com.example.dto.request.LanguageCreateRequest;
import com.example.dto.request.response.LanguageResponse;
import com.example.entity.Language;
import com.example.mapper.LanguageMapper;
import com.example.repository.LanguageRepository;
import com.example.service.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository languageRepository;
    private final LanguageMapper languageMapper;

    @Override
    public LanguageResponse createLanguage(LanguageCreateRequest request) {

        Language language = languageMapper.toEntity(request);

        Language savedLanguage = languageRepository.save(language);

        return languageMapper.toResponse(savedLanguage);
    }

    @Override
    public LanguageResponse getLanguageById(Long id) {

        Language language = languageRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Language not found with id: " + id));

        return languageMapper.toResponse(language);
    }

    @Override
    public List<LanguageResponse> getAllLanguages() {

        return languageRepository.findAll()
                .stream()
                .map(languageMapper::toResponse)
                .toList();
    }
}