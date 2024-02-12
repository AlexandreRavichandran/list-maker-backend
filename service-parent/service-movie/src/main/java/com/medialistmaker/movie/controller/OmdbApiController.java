package com.medialistmaker.movie.controller;

import com.medialistmaker.movie.connector.omdb.OmdbConnectorProxy;
import com.medialistmaker.movie.dto.externalapi.omdbapi.collection.MovieElementListDTO;
import com.medialistmaker.movie.dto.externalapi.omdbapi.item.MovieElementDTO;
import com.medialistmaker.movie.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.movie.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.movie.exception.servicenotavailableexception.ServiceNotAvailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("/api/movies/omdbapi")
public class OmdbApiController {

    private static final Integer OMDB_ELEMENT_PER_PAGE = 10;

    @Autowired
    OmdbConnectorProxy omdbConnectorProxy;

    @GetMapping
    public ResponseEntity<MovieElementListDTO> browseByQueryAndFilter(
            @RequestParam("name") String movieName,
            @RequestParam(value = "year", required = false) String year,
            @RequestParam("index") Integer index)
            throws CustomBadRequestException, ServiceNotAvailableException {

        //To transform index to page, we have to add 10 (pagination start at 1)
        Integer nextIndex = index + 10;

        Integer page = nextIndex.equals(0) || nextIndex < OMDB_ELEMENT_PER_PAGE ? 1 : nextIndex / OMDB_ELEMENT_PER_PAGE;

        MovieElementListDTO results = this.omdbConnectorProxy.getByQuery(movieName, year, page);

        results.setCurrentIndex(index);
        results.setElementsPerPage(OMDB_ELEMENT_PER_PAGE);

        if(isNull(results.getSearchResults()) || results.getSearchResults().isEmpty()) {
            results.setTotalResults(0);
        }

        return new ResponseEntity<>(
                results,
                HttpStatus.OK
        );

    }

    @GetMapping("/apicodes/{apicode}")
    public ResponseEntity<MovieElementDTO> getByApiCode(@PathVariable("apicode") String apiCode)
            throws CustomNotFoundException, CustomBadRequestException, ServiceNotAvailableException {

        MovieElementDTO movieElementDTO = this.omdbConnectorProxy.getByApiCode(apiCode);

        if(isNull(movieElementDTO.getApiCode())) {
            throw new CustomNotFoundException("Movie not found");
        }

        return new ResponseEntity<>(
                movieElementDTO,
                HttpStatus.OK
        );

    }
}
