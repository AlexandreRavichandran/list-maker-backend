package com.medialistmaker.movie.controller;

import com.medialistmaker.movie.connector.list.ListConnectorProxy;
import com.medialistmaker.movie.connector.omdb.OmdbConnectorProxy;
import com.medialistmaker.movie.domain.Movie;
import com.medialistmaker.movie.dto.externalapi.omdbapi.collection.MovieElementListDTO;
import com.medialistmaker.movie.dto.externalapi.omdbapi.item.MovieElementDTO;
import com.medialistmaker.movie.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.movie.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.movie.exception.servicenotavailableexception.ServiceNotAvailableException;
import com.medialistmaker.movie.service.movie.MovieServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Objects.nonNull;

@RestController
@RequestMapping("/api/movies/omdbapi")
public class OmdbApiController {

    @Autowired
    OmdbConnectorProxy omdbConnectorProxy;

    @Autowired
    ListConnectorProxy listConnectorProxy;

    @Autowired
    MovieServiceImpl movieService;

    @GetMapping("/names/{name}")
    public ResponseEntity<MovieElementListDTO> getByMovieName(@PathVariable("name") String name)
            throws CustomBadRequestException, ServiceNotAvailableException {

        return new ResponseEntity<>(
                this.omdbConnectorProxy.getByQuery(name),
                HttpStatus.OK
        );

    }

    @GetMapping("/apicodes/{apicode}")
    public ResponseEntity<MovieElementDTO> getByApiCode(@PathVariable("apicode") String apiCode)
            throws CustomBadRequestException, ServiceNotAvailableException {

        Boolean isAlreadyInList;

        MovieElementDTO movieElementDTO = this.omdbConnectorProxy.getByApiCode(apiCode);

        try {

            Movie movie = this.movieService.readByApiCode(movieElementDTO.getApiCode());

            if(nonNull(movie)) {
                isAlreadyInList = this.listConnectorProxy.isMovieIdAlreadyInList(movie.getId());
            } else {
                isAlreadyInList = Boolean.FALSE;
            }

        } catch (CustomNotFoundException e) {
            isAlreadyInList = Boolean.FALSE;
        }


        movieElementDTO.setIsAlreadyInList(isAlreadyInList);
        return new ResponseEntity<>(
                movieElementDTO,
                HttpStatus.OK
        );

    }
}
