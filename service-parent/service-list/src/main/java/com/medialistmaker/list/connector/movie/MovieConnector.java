package com.medialistmaker.list.connector.movie;

import com.medialistmaker.list.dto.movie.MovieDTO;
import com.medialistmaker.list.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.list.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.list.exception.servicenotavailableexception.ServiceNotAvailableException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "movie", url = "/api/movies")
public interface MovieConnector {

    @GetMapping("/apicodes/{apicode}")
    MovieDTO getByApiCode(@PathVariable("apicode") String apiCode) throws CustomNotFoundException, ServiceNotAvailableException;

    @PostMapping("/apicodes/{apicode}")
    MovieDTO saveByApiCode(@PathVariable("apicode") String apiCode) throws CustomBadRequestException, ServiceNotAvailableException;

    @DeleteMapping("/{id}")
    MovieDTO deleteById(@PathVariable("id") Long id) throws CustomNotFoundException, ServiceNotAvailableException;
}
