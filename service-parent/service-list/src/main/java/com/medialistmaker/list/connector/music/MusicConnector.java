package com.medialistmaker.list.connector.music;

import com.medialistmaker.list.dto.music.MusicDTO;
import com.medialistmaker.list.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.list.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.list.exception.servicenotavailableexception.ServiceNotAvailableException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "music", path = "/api/musics")
public interface MusicConnector {

    @GetMapping("/deezerapi/albums/apicodes/{apicode}")
    MusicDTO getAlbumByApiCode(@PathVariable("apicode") String apiCode) throws CustomNotFoundException, ServiceNotAvailableException;

    @GetMapping("/deezerapi/songs/apicodes/{apicode}")
    MusicDTO getSongByApiCode(@PathVariable("apicode") String apiCode) throws CustomNotFoundException, ServiceNotAvailableException;

    @PostMapping("/apicodes/{apicode}")
    MusicDTO saveByApiCode(@RequestParam("type") Integer type, @PathVariable("apicode") String apiCode)
            throws CustomBadRequestException, ServiceNotAvailableException;

    @DeleteMapping("/{musicId}")
    MusicDTO deleteById(@PathVariable("musicId") Long id) throws CustomNotFoundException, ServiceNotAvailableException;
}
