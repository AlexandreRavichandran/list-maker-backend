package com.medialistmaker.music.connector.deezer.album;

import com.medialistmaker.music.connector.deezer.DeezerConnector;
import com.medialistmaker.music.dto.externalapi.deezerapi.AlbumElementDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.TrackListDTO;
import com.medialistmaker.music.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.music.exception.servicenotavailableexception.ServiceNotAvailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

@Component
public class DeezerAlbumConnectorProxy implements DeezerConnector<AlbumElementDTO> {

    @Autowired
    DeezerAlbumConnector deezerAlbumConnector;

    public AlbumElementDTO getByApiCode(String apiCode) throws CustomBadRequestException, ServiceNotAvailableException {
        return this.deezerAlbumConnector.getByApiCode(apiCode);
    }

    public TrackListDTO getTrackListByAlbumApiCode(@PathVariable("apicode") String apiCode)
            throws ServiceNotAvailableException, CustomBadRequestException {
        return this.deezerAlbumConnector.getTrackListByAlbumApiCode(apiCode);
    }

}
