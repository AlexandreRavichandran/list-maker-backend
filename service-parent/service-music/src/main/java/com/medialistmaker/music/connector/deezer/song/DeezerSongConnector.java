package com.medialistmaker.music.connector.deezer.song;

import com.medialistmaker.music.dto.externalapi.deezerapi.SongElementDTO;
import com.medialistmaker.music.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.music.exception.servicenotavailableexception.ServiceNotAvailableException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "deezerSong", url = "https://api.deezer.com/track" )
public interface DeezerSongConnector {

    @GetMapping("/{apicode}")
    SongElementDTO getByApiCode(@PathVariable("apicode") String apiCode)
            throws CustomBadRequestException, ServiceNotAvailableException;

}
