package com.medialistmaker.movie.controller;

import com.medialistmaker.movie.dto.externalapi.omdbapi.collection.MovieElementListDTO;
import com.medialistmaker.movie.dto.externalapi.omdbapi.item.MovieElementDTO;
import com.medialistmaker.movie.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.movie.service.externalapi.omdb.OmdbExternalApiServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/movies/omdbapi")
public class OmdbApiController {

    @Autowired
    OmdbExternalApiServiceImpl omdbService;

    @GetMapping("/names/{name}")
    public ResponseEntity<MovieElementListDTO> getByMovieName(@PathVariable("name") String name) throws CustomBadRequestException {

        return new ResponseEntity<>(
                this.omdbService.getByMovieName(name),
                HttpStatus.OK
        );

    }

    @GetMapping("/apicodes/{apicode}")
    public ResponseEntity<MovieElementDTO> getByApiCode(@PathVariable("apicode") String apiCode) throws CustomBadRequestException {

        return new ResponseEntity<>(
                this.omdbService.getByApiCode(apiCode),
                HttpStatus.OK
        );

    }
}
