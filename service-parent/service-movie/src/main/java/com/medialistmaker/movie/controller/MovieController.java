package com.medialistmaker.movie.controller;

import com.medialistmaker.movie.dto.MovieAddDTO;
import com.medialistmaker.movie.dto.MovieDTO;
import com.medialistmaker.movie.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.movie.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.movie.exception.servicenotavailableexception.ServiceNotAvailableException;
import com.medialistmaker.movie.service.movie.MovieServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final ModelMapper modelMapper;

    private final MovieServiceImpl movieService;

    public MovieController(
            ModelMapper modelMapper,
            MovieServiceImpl movieService
    ) {
        this.modelMapper = modelMapper;
        this.movieService = movieService;
    }

    @GetMapping
    public ResponseEntity<List<MovieDTO>> browseByIds(@RequestParam("movieIds") List<Long> movieIds) {

        return new ResponseEntity<>(
                this.movieService.browseByIds(movieIds)
                        .stream()
                        .map(movie -> this.modelMapper.map(movie, MovieDTO.class))
                        .toList(),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<MovieDTO> addFromApiCode(@RequestBody MovieAddDTO movieAddDTO)
            throws CustomBadRequestException, ServiceNotAvailableException {

        return new ResponseEntity<>(
                this.modelMapper.map(this.movieService.addByApiCode(movieAddDTO.getApiCode()), MovieDTO.class),
                HttpStatus.CREATED
        );

    }

    @GetMapping("/apicodes/{apicode}")
    public ResponseEntity<MovieDTO> readByApiCode(@PathVariable("apicode") String apiCode)
            throws CustomNotFoundException {

        return new ResponseEntity<>(
                this.modelMapper.map(this.movieService.readByApiCode(apiCode), MovieDTO.class),
                HttpStatus.OK
        );
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDTO> readById(@PathVariable("movieId") Long movieId)
            throws CustomNotFoundException {

        return new ResponseEntity<>(
                this.modelMapper.map(this.movieService.readById(movieId), MovieDTO.class),
                HttpStatus.OK
        );
    }


    @DeleteMapping("/{movieId}")
    public ResponseEntity<MovieDTO> deleteById(@PathVariable("movieId") Long movieId)
            throws CustomNotFoundException {

        return new ResponseEntity<>(
                this.modelMapper.map(this.movieService.deleteById(movieId), MovieDTO.class),
                HttpStatus.OK
        );
    }
}
