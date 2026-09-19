package com.example.service.impl;

import com.example.dto.request.PersonCreateRequest;
import com.example.dto.request.response.PersonResponse;
import com.example.entity.Person;
import com.example.mapper.PersonMapper;
import com.example.repository.PersonRepository;
import com.example.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    @Override
    public PersonResponse createPerson(PersonCreateRequest request) {

        Person person = personMapper.toEntity(request);

        Person savedPerson = personRepository.save(person);

        return personMapper.toResponse(savedPerson);
    }

    @Override
    public PersonResponse getPersonById(Long id) {

        Person person = personRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Person not found with id: " + id));

        return personMapper.toResponse(person);
    }

    @Override
    public List<PersonResponse> getAllPersons() {

        return personRepository.findAll()
                .stream()
                .map(personMapper::toResponse)
                .toList();
    }
}
