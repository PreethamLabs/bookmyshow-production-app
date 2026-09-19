package com.example.controller;

import com.example.dto.request.PersonCreateRequest;
import com.example.dto.request.response.PersonResponse;
import com.example.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PersonResponse> createPerson(
            @RequestBody PersonCreateRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(personService.createPerson(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonResponse> getPersonById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                personService.getPersonById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<PersonResponse>> getAllPersons() {

        return ResponseEntity.ok(
                personService.getAllPersons()
        );
    }
}
