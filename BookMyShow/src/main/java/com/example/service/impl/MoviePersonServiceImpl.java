package com.example.service.impl;

import com.example.dto.request.MoviePersonCreateRequest;
import com.example.dto.request.response.MoviePersonResponse;
import com.example.entity.Movie;
import com.example.entity.MoviePerson;
import com.example.entity.Person;
import com.example.mapper.MoviePersonMapper;
import com.example.repository.MoviePersonRepository;
import com.example.repository.MovieRepository;
import com.example.repository.PersonRepository;
import com.example.service.MoviePersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MoviePersonServiceImpl implements MoviePersonService {

    private final MoviePersonRepository moviePersonRepository;
    private final MovieRepository movieRepository;
    private final PersonRepository personRepository;
    private final MoviePersonMapper moviePersonMapper;

    @Override
    public MoviePersonResponse createMoviePerson(
            MoviePersonCreateRequest request) {

        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Movie not found with id: "
                                        + request.getMovieId()));

        Person person = personRepository.findById(request.getPersonId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Person not found with id: "
                                        + request.getPersonId()));

        MoviePerson moviePerson =
                moviePersonMapper.toEntity(request);

        moviePerson.setMovie(movie);
        moviePerson.setPerson(person);

        MoviePerson savedMoviePerson =
                moviePersonRepository.save(moviePerson);

        return moviePersonMapper.toResponse(savedMoviePerson);
    }

    @Override
    public MoviePersonResponse getMoviePersonById(Long id) {

        MoviePerson moviePerson =
                moviePersonRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "MoviePerson not found with id: "
                                                + id));

        return moviePersonMapper.toResponse(moviePerson);
    }

    @Override
    public List<MoviePersonResponse> getAllMoviePersons() {

        return moviePersonRepository.findAll()
                .stream()
                .map(moviePersonMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteMoviePerson(Long id) {

        if (!moviePersonRepository.existsById(id)) {
            throw new RuntimeException(
                    "MoviePerson not found with id: " + id);
        }

        moviePersonRepository.deleteById(id);
    }
}