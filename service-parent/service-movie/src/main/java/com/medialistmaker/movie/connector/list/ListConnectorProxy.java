package com.medialistmaker.movie.connector.list;

import com.medialistmaker.movie.exception.servicenotavailableexception.ServiceNotAvailableException;
import org.springframework.stereotype.Component;

@Component
public class ListConnectorProxy {

    private final ListConnector listConnector;

    public ListConnectorProxy(
            ListConnector listConnector
    ) {
        this.listConnector = listConnector;
    }

    public Boolean isMovieIdAlreadyInList(Long movieId) throws ServiceNotAvailableException {
     return this.listConnector.isMovieIdAlreadyInList(movieId);
    }
}
