package com.medialistmaker.music.connector.list;

import com.medialistmaker.music.exception.servicenotavailableexception.ServiceNotAvailableException;
import org.springframework.stereotype.Component;

@Component
public class ListConnectorProxy {

    private final ListConnector listConnector;

    public ListConnectorProxy(
            ListConnector listConnector
    ) {
        this.listConnector = listConnector;
    }

    public Boolean isMusicIdAlreadyInList(Long musicId) throws ServiceNotAvailableException {
        return this.listConnector.isMusicIdAlreadyInList(musicId);
    }

}
