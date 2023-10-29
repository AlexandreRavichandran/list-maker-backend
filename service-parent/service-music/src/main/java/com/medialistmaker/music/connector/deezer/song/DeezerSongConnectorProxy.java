package com.medialistmaker.music.connector.deezer.song;

import com.medialistmaker.music.dto.externalapi.deezerapi.SongElementDTO;
import com.medialistmaker.music.exception.badrequestexception.CustomBadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeezerSongConnectorProxy {

    @Autowired
    DeezerSongConnector deezerSongConnector;

    public SongElementDTO getByApiCode(String apiCode) throws CustomBadRequestException {
        return this.deezerSongConnector.getByApiCode(apiCode);
    }
}
