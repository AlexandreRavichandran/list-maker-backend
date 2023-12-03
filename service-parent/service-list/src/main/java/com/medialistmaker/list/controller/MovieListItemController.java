package com.medialistmaker.list.controller;

import com.medialistmaker.list.dto.movie.MovieListItemAddDTO;
import com.medialistmaker.list.dto.movie.MovieListItemDTO;
import com.medialistmaker.list.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.list.exception.entityduplicationexception.CustomEntityDuplicationException;
import com.medialistmaker.list.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.list.exception.servicenotavailableexception.ServiceNotAvailableException;
import com.medialistmaker.list.service.movielistitem.MovieListItemServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/lists/movies")
public class MovieListItemController {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    MovieListItemServiceImpl movieListService;

    @GetMapping
    public ResponseEntity<List<MovieListItemDTO>> getByAppUserId() {

        return new ResponseEntity<>(
                this.movieListService
                        .getByAppUserId(1L)
                        .stream()
                        .map(listItem -> this.modelMapper.map(listItem, MovieListItemDTO.class))
                        .toList(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<Boolean> isMovieInAppUserList(@PathVariable("movieId") Long movieId) {

        return new ResponseEntity<>(
                this.movieListService.isMovieUsedInOtherList(movieId),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<MovieListItemDTO> add(@RequestBody MovieListItemAddDTO listItemDTO)
            throws CustomBadRequestException, CustomEntityDuplicationException, ServiceNotAvailableException {

        return new ResponseEntity<>(
                this.modelMapper.map(this.movieListService.add(listItemDTO), MovieListItemDTO.class),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/{listItemId}")
    public ResponseEntity<MovieListItemDTO> deleteById(@PathVariable("listItemId") Long listItemId)
            throws CustomNotFoundException, ServiceNotAvailableException {

        return new ResponseEntity<>(
                this.modelMapper.map(this.movieListService.deleteById(listItemId), MovieListItemDTO.class),
                HttpStatus.OK
        );
    }
}
