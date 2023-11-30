package com.medialistmaker.music.connector.deezer.artist;

import com.medialistmaker.music.dto.externalapi.deezerapi.AlbumListDTO;
import com.medialistmaker.music.exception.badrequestexception.CustomBadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeezerArtistConnectorProxy {

    @Autowired
    DeezerArtistConnector deezerArtistConnector;

    public AlbumListDTO getAlbumListByArtistId(Long artistId) throws CustomBadRequestException {
        return this.deezerArtistConnector.getAlbumListByArtistId(artistId);
    }
}
