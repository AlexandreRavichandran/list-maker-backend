package com.medialistmaker.movie.controller;

import com.medialistmaker.movie.dto.MovieDTO;
import com.medialistmaker.movie.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.movie.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.movie.exception.servicenotavailableexception.ServiceNotAvailableException;
import com.medialistmaker.movie.service.movie.MovieServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    MovieServiceImpl movieService;

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

    @PostMapping("/apicodes/{apicode}")
    public ResponseEntity<MovieDTO> addFromApiCode(@PathVariable("apicode") String apiCode)
            throws CustomBadRequestException, ServiceNotAvailableException {

        return new ResponseEntity<>(
                this.modelMapper.map(this.movieService.addByApiCode(apiCode), MovieDTO.class),
                HttpStatus.CREATED
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
