package com.medialistmaker.movie.connector.list;

import com.medialistmaker.movie.exception.servicenotavailableexception.ServiceNotAvailableException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "list", path = "/api/lists/movies")
public interface ListConnector {

    @GetMapping("/{movieId}")
    Boolean isMovieIdAlreadyInList(@PathVariable("movieId") Long movieId) throws ServiceNotAvailableException;

}
