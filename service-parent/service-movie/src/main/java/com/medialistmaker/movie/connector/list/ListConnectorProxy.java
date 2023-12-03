package com.medialistmaker.movie.connector.list;

import com.medialistmaker.movie.exception.servicenotavailableexception.ServiceNotAvailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ListConnectorProxy {

    @Autowired
    ListConnector listConnector;

    public Boolean isMovieIdAlreadyInList(Long movieId) throws ServiceNotAvailableException {
     return this.listConnector.isMovieIdAlreadyInList(movieId);
    }
}
