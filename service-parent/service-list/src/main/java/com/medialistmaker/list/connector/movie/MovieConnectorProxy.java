package com.medialistmaker.list.connector.movie;

import com.medialistmaker.list.dto.movie.MovieAddDTO;
import com.medialistmaker.list.dto.movie.MovieDTO;
import com.medialistmaker.list.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.list.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.list.exception.servicenotavailableexception.ServiceNotAvailableException;
import org.springframework.stereotype.Component;

@Component
public class MovieConnectorProxy {

    private final MovieConnector movieConnector;

    public MovieConnectorProxy(
            MovieConnector movieConnector
    ) {
        this.movieConnector = movieConnector;
    }

    public MovieDTO saveByApiCode(MovieAddDTO movieAddDTO) throws CustomBadRequestException, ServiceNotAvailableException {
        return this.movieConnector.saveByApiCode(movieAddDTO);
    }

    public MovieDTO getByApiCode(String apiCode) throws CustomNotFoundException, ServiceNotAvailableException {
        return this.movieConnector.getByApiCode(apiCode);
    }

    public MovieDTO deleteById(Long id) throws CustomNotFoundException, ServiceNotAvailableException {
        return this.movieConnector.deleteById(id);
    }
}
