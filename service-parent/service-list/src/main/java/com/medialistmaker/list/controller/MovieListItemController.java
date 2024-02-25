package com.medialistmaker.list.controller;

import com.medialistmaker.list.domain.MovieListItem;
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

import static java.util.Objects.isNull;

@RestController
@RequestMapping("api/lists/movies")
public class MovieListItemController extends AbstractController {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    MovieListItemServiceImpl movieListService;

    @GetMapping
    public ResponseEntity<List<MovieListItemDTO>> getByAppUserId() {

        return new ResponseEntity<>(
                this.movieListService
                        .getByAppUserId(this.getCurrentLoggedAppUserId())
                        .stream()
                        .map(listItem -> this.modelMapper.map(listItem, MovieListItemDTO.class))
                        .toList(),
                HttpStatus.OK
        );
    }

    @GetMapping("/random")
    public ResponseEntity<MovieListItemDTO> getRandomInAppUserList() {

        MovieListItem randomMovieLisItem = this.movieListService.getRandomInAppUserList(this.getCurrentLoggedAppUserId());

        if (isNull(randomMovieLisItem)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
                this.modelMapper.map(randomMovieLisItem, MovieListItemDTO.class),
                HttpStatus.OK
        );

    }

    @GetMapping("/latest")
    public ResponseEntity<List<MovieListItemDTO>> getLatestAddedByAppUserId() {

        return new ResponseEntity<>(
                this.movieListService
                        .getLatestAddedByAppUserId(this.getCurrentLoggedAppUserId())
                        .stream()
                        .map(listItem -> this.modelMapper.map(listItem, MovieListItemDTO.class))
                        .toList(),
                HttpStatus.OK
        );
    }


    @GetMapping("/apicode/{apicode}")
    public ResponseEntity<Boolean> isMovieAlreadyInAppUserList(@PathVariable("apicode") String apiCode)
            throws ServiceNotAvailableException {

        return new ResponseEntity<>(
                this.movieListService.isMovieApiCodeAlreadyInAppUserMovieList(this.getCurrentLoggedAppUserId(), apiCode),
                HttpStatus.OK
        );

    }

    @PutMapping("/{listItemId}")
    public ResponseEntity<List<MovieListItemDTO>> editSortingOrder(
            @PathVariable("listItemId") Long listItemId,
            @RequestBody Integer nextSortingOrder) throws CustomNotFoundException {

        return new ResponseEntity<>(
                this.movieListService.editSortingOrder(this.getCurrentLoggedAppUserId(), listItemId, nextSortingOrder)
                        .stream()
                        .map(listItem -> this.modelMapper.map(listItem, MovieListItemDTO.class))
                        .toList(),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<MovieListItemDTO> add(@RequestBody MovieListItemAddDTO listItemDTO)
            throws CustomBadRequestException, CustomEntityDuplicationException, ServiceNotAvailableException {

        listItemDTO.setAppUserId(this.getCurrentLoggedAppUserId());

        return new ResponseEntity<>(
                this.modelMapper.map(this.movieListService.add(listItemDTO), MovieListItemDTO.class),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/{listItemId}")
    public ResponseEntity<MovieListItemDTO> deleteById(@PathVariable("listItemId") Long listItemId)
            throws CustomNotFoundException, ServiceNotAvailableException {

        return new ResponseEntity<>(
                this.modelMapper.map(
                        this.movieListService.deleteById(
                                this.getCurrentLoggedAppUserId(),
                                listItemId
                        ), MovieListItemDTO.class),
                HttpStatus.OK
        );
    }
}
