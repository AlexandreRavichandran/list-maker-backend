package com.medialistmaker.music.connector.deezer.search;

import com.medialistmaker.music.dto.externalapi.deezerapi.search.list.AlbumSearchListDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.search.list.SongSearchListDTO;
import com.medialistmaker.music.exception.badrequestexception.CustomBadRequestException;
import org.springframework.stereotype.Component;

@Component
public class DeezerSearchConnectorProxy {

    private final DeezerSearchConnector deezerSearchConnector;

    public DeezerSearchConnectorProxy(
            DeezerSearchConnector deezerSearchConnector
    ) {
        this.deezerSearchConnector = deezerSearchConnector;
    }

    public AlbumSearchListDTO getAlbumByQuery(String query, Integer index) throws CustomBadRequestException {
        return this.deezerSearchConnector.getAlbumByQuery(query, index);
    }

    public AlbumSearchListDTO getAlbumByQuery(String query) throws CustomBadRequestException {
        return this.deezerSearchConnector.getAlbumByQuery(query, 0);
    }

    public SongSearchListDTO getSongByQuery(String query) throws CustomBadRequestException {
        return this.deezerSearchConnector.getSongByQuery(query);
    }
}
