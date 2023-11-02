package com.medialistmaker.list.connector.music;

import com.medialistmaker.list.dto.music.MusicDTO;
import com.medialistmaker.list.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.list.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.list.exception.servicenotavailableexception.ServiceNotAvailableException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "music")
public interface MusicConnector {

    @GetMapping("/api/movies/apicodes/{apicode}")
    MusicDTO getAlbumByApiCode(@PathVariable("apicode") String apiCode) throws CustomNotFoundException, ServiceNotAvailableException;

    @GetMapping("/api/movies/apicodes/{apicode}")
    MusicDTO getSongByApiCode(@PathVariable("apicode") String apiCode) throws CustomNotFoundException, ServiceNotAvailableException;

    @PostMapping("/api/movies/apicode/{apicode}")
    MusicDTO saveByApiCode(@RequestParam("type") Integer type, @PathVariable("apicode") String apiCode)
            throws CustomBadRequestException, ServiceNotAvailableException;

}
