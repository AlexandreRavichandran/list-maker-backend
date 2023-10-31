package com.medialistmaker.music.connector.deezer.song;

import com.medialistmaker.music.connector.deezer.DeezerConnector;
import com.medialistmaker.music.dto.externalapi.deezerapi.SongElementDTO;
import com.medialistmaker.music.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.music.exception.servicenotavailableexception.ServiceNotAvailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeezerSongConnectorProxy implements DeezerConnector<SongElementDTO> {

    @Autowired
    DeezerSongConnector deezerSongConnector;

    public SongElementDTO getByApiCode(String apiCode) throws CustomBadRequestException, ServiceNotAvailableException {
        return this.deezerSongConnector.getByApiCode(apiCode);
    }
}
