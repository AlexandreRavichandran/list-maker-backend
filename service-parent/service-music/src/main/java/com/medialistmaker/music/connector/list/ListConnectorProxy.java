package com.medialistmaker.music.connector.list;

import com.medialistmaker.music.exception.servicenotavailableexception.ServiceNotAvailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ListConnectorProxy {

    @Autowired
    ListConnector listConnector;

    public Boolean isMusicIdAlreadyInList(Long musicId) throws ServiceNotAvailableException {
        return this.listConnector.isMusicIdAlreadyInList(musicId);
    }

}
