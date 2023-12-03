package com.medialistmaker.movie.connector.omdb;

import com.medialistmaker.movie.dto.externalapi.omdbapi.collection.MovieElementListDTO;
import com.medialistmaker.movie.dto.externalapi.omdbapi.item.MovieElementDTO;
import com.medialistmaker.movie.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.movie.exception.servicenotavailableexception.ServiceNotAvailableException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "omdbapi", url = "http://www.omdbapi.com/")
public interface OmdbConnector {

    @GetMapping
    MovieElementDTO getMovieByApiCode(
            @RequestParam("i") String apiCode,
            @RequestParam("apiKey") String apiKey,
            @RequestParam("plot") String plot)
            throws CustomBadRequestException, ServiceNotAvailableException;

    @GetMapping
    MovieElementListDTO getMoviesByQuery(@RequestParam("s") String query, @RequestParam("apiKey") String apiKey)
            throws CustomBadRequestException, ServiceNotAvailableException;

}
