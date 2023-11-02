package com.medialistmaker.list.connector.music;

import com.medialistmaker.list.dto.music.MusicDTO;
import com.medialistmaker.list.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.list.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.list.exception.servicenotavailableexception.ServiceNotAvailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MusicConnectorProxy {

    @Autowired
    MusicConnector musicConnector;

    public MusicDTO saveByApiCode(Integer type, String apiCode) throws CustomBadRequestException, ServiceNotAvailableException {
        return this.musicConnector.saveByApiCode(type, apiCode);
    }

    public MusicDTO getAlbumByApiCode(String apiCode) throws CustomNotFoundException, ServiceNotAvailableException {
        return this.musicConnector.getAlbumByApiCode(apiCode);
    }

    public MusicDTO getSongByApiCode(String apiCode) throws CustomNotFoundException, ServiceNotAvailableException {
        return this.musicConnector.getSongByApiCode(apiCode);
    }
}
