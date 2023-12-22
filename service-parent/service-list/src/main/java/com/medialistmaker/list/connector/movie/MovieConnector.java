package com.medialistmaker.list.connector.movie;

import com.medialistmaker.list.dto.movie.MovieAddDTO;
import com.medialistmaker.list.dto.movie.MovieDTO;
import com.medialistmaker.list.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.list.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.list.exception.servicenotavailableexception.ServiceNotAvailableException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "movie", path = "/api/movies")
public interface MovieConnector {

    @GetMapping("/apicodes/{apicode}")
    MovieDTO getByApiCode(@PathVariable("apicode") String apiCode) throws CustomNotFoundException, ServiceNotAvailableException;

    @PostMapping
    MovieDTO saveByApiCode(@RequestBody MovieAddDTO movieAddDTO) throws CustomBadRequestException, ServiceNotAvailableException;

    @DeleteMapping("/{id}")
    MovieDTO deleteById(@PathVariable("id") Long id) throws CustomNotFoundException, ServiceNotAvailableException;
}
