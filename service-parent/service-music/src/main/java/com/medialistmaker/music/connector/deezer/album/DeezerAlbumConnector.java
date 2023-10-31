package com.medialistmaker.music.connector.deezer.album;

import com.medialistmaker.music.dto.externalapi.deezerapi.AlbumElementDTO;
import com.medialistmaker.music.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.music.exception.servicenotavailableexception.ServiceNotAvailableException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "deezerAlbum", url = "https://api.deezer.com/album")
public interface DeezerAlbumConnector {

    @GetMapping("/{apicode}")
    AlbumElementDTO getByApiCode(@PathVariable("apicode") String apiCode)
            throws CustomBadRequestException, ServiceNotAvailableException;

}
