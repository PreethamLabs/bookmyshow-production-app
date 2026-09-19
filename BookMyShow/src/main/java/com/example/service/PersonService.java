package com.example.service;

import com.example.dto.request.PersonCreateRequest;
import com.example.dto.request.response.PersonResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface PersonService {

    PersonResponse createPerson(PersonCreateRequest request);

    PersonResponse getPersonById(Long id);

    List<PersonResponse> getAllPersons();
}
