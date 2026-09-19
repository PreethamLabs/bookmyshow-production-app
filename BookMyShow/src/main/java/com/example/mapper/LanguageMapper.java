package com.example.mapper;

import com.example.dto.request.LanguageCreateRequest;
import com.example.dto.request.response.LanguageResponse;
import com.example.entity.Language;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LanguageMapper {

    @Mapping(target = "id", ignore = true)
    Language toEntity(LanguageCreateRequest request);

    LanguageResponse toResponse(Language language);
}
