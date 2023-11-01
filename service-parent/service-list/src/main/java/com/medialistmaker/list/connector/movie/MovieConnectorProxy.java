package com.medialistmaker.list.connector.movie;

import com.medialistmaker.list.dto.movie.MovieDTO;
import com.medialistmaker.list.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.list.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.list.exception.servicenotavailableexception.ServiceNotAvailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MovieConnectorProxy {

    @Autowired
    MovieConnector movieConnector;

    public MovieDTO saveByApiCode(String apiCode) throws CustomBadRequestException, ServiceNotAvailableException {
        return this.movieConnector.saveByApiCode(apiCode);
    }

    public MovieDTO getByApiCode(String apiCode) throws CustomNotFoundException, ServiceNotAvailableException {
        return this.movieConnector.getByApiCode(apiCode);
    }
}
