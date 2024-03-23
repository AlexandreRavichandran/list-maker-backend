package com.medialistmaker.list.connector.music;

import com.medialistmaker.list.dto.music.MusicAddDTO;
import com.medialistmaker.list.dto.music.MusicDTO;
import com.medialistmaker.list.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.list.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.list.exception.servicenotavailableexception.ServiceNotAvailableException;
import org.springframework.stereotype.Component;

@Component
public class MusicConnectorProxy {

    private final MusicConnector musicConnector;

    public MusicConnectorProxy(
            MusicConnector musicConnector
    ) {
        this.musicConnector = musicConnector;
    }

    public MusicDTO getMusicByApiCodeAndType(String apiCode, Integer type)
            throws CustomNotFoundException, ServiceNotAvailableException {
        return this.musicConnector.getMusicByApiCodeAndType(apiCode, type);
    }

    public MusicDTO saveByApiCode(MusicAddDTO musicAddDTO) throws CustomBadRequestException, ServiceNotAvailableException {
        return this.musicConnector.saveByApiCode(musicAddDTO);
    }

    public MusicDTO deleteById(Long id) throws CustomNotFoundException, ServiceNotAvailableException {
        return this.musicConnector.deleteById(id);
    }
}
