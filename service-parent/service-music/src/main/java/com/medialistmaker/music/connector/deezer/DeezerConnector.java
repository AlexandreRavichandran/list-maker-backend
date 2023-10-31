package com.medialistmaker.music.connector.deezer;

import com.medialistmaker.music.dto.externalapi.deezerapi.MusicElementDTO;
import com.medialistmaker.music.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.music.exception.servicenotavailableexception.ServiceNotAvailableException;

public interface DeezerConnector<T extends MusicElementDTO> {

    T getByApiCode(String apiCode) throws CustomBadRequestException, ServiceNotAvailableException;
}
