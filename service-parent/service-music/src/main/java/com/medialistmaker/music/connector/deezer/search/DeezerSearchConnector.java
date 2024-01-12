package com.medialistmaker.music.connector.deezer.search;

import com.medialistmaker.music.dto.externalapi.deezerapi.search.list.AlbumSearchListDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.search.list.SongSearchListDTO;
import com.medialistmaker.music.exception.badrequestexception.CustomBadRequestException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "deezerSearch", url = "https://api.deezer.com/search")
public interface DeezerSearchConnector {

    @GetMapping("/album")
    AlbumSearchListDTO getAlbumByQuery(@RequestParam("q") String query, @RequestParam("index") Integer index)
            throws CustomBadRequestException;

    @GetMapping("/track")
    SongSearchListDTO getSongByQuery(@RequestParam("q") String query) throws CustomBadRequestException;
}
