package com.example.service.impl;

import com.example.dto.request.ShowCreateRequest;
import com.example.dto.request.ShowUpdateRequest;
import com.example.dto.request.response.ShowResponse;
import com.example.entity.*;
import com.example.enums.SeatStatus;
import com.example.mapper.ShowMapper;
import com.example.repository.*;
import com.example.service.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowServiceImpl implements ShowService {

    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;
    private final ScreenRepository screenRepository;
    private final ShowMapper showMapper;
    private final ShowSeatRepository showSeatRepository;
    private final SeatRepository seatRepository;

    @Override

    @Transactional
    public ShowResponse createShow(ShowCreateRequest request) {

        Show show = showMapper.toEntity(request);

        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        Screen screen = screenRepository.findById(request.getScreenId())
                .orElseThrow(() -> new RuntimeException("Screen not found"));

        show.setMovie(movie);
        show.setScreen(screen);

        Show savedShow = showRepository.save(show);

        List<Seat> seats = seatRepository.findByScreenId(screen.getId());

        List<ShowSeat> showSeats = seats.stream()
                .map(seat -> ShowSeat.builder()
                        .show(savedShow)
                        .seat(seat)
                        .seatStatus(SeatStatus.AVAILABLE)
                        .build())
                .toList();

        showSeatRepository.saveAll(showSeats);

        return showMapper.toResponse(savedShow);
    }

    @Override
    public ShowResponse getShowById(Long id) {

        Show show = showRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Show not found"));

        return showMapper.toResponse(show);
    }

    @Override
    public List<ShowResponse> getAllShows() {

        return showRepository.findAll()
                .stream()
                .map(showMapper::toResponse)
                .toList();
    }

    @Override
    public ShowResponse updateShow(
            Long id,
            ShowUpdateRequest request) {

        Show show = showRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Show not found"));

        showMapper.updateEntity(request, show);

        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        Screen screen = screenRepository.findById(request.getScreenId())
                .orElseThrow(() -> new RuntimeException("Screen not found"));

        show.setMovie(movie);
        show.setScreen(screen);

        Show updatedShow = showRepository.save(show);

        return showMapper.toResponse(updatedShow);
    }

    @Override
    @Transactional
    public void deleteShow(Long id) {

        Show show = showRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Show not found"));

        showSeatRepository.deleteByShowId(id);

        showRepository.delete(show);
    }
}
