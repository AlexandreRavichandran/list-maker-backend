package com.medialistmaker.music.connector.deezer.search;

import com.medialistmaker.music.dto.externalapi.deezerapi.search.list.AlbumSearchListDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.search.list.SongSearchListDTO;
import com.medialistmaker.music.exception.badrequestexception.CustomBadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeezerSearchConnectorProxy {

    @Autowired
    DeezerSearchConnector deezerSearchConnector;

    public AlbumSearchListDTO getAlbumByQuery(String query) throws CustomBadRequestException {
        return this.deezerSearchConnector.getAlbumByQuery(query);
    }

    public SongSearchListDTO getSongByQuery(String query) throws CustomBadRequestException {
        return this.deezerSearchConnector.getSongByQuery(query);
    }
}
