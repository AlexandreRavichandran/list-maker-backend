package com.medialistmaker.music.connector.deezer.album;

import com.medialistmaker.music.dto.externalapi.deezerapi.AlbumElementDTO;
import com.medialistmaker.music.exception.badrequestexception.CustomBadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeezerAlbumConnectorProxy {

    @Autowired
    DeezerAlbumConnector deezerAlbumConnector;

    public AlbumElementDTO getByApiCode(String apiCode) throws CustomBadRequestException {
        return this.deezerAlbumConnector.getByApiCode(apiCode);
    }
}
