package com.medialistmaker.music.connector.deezer.artist;

import com.medialistmaker.music.dto.externalapi.deezerapi.AlbumListDTO;
import com.medialistmaker.music.exception.badrequestexception.CustomBadRequestException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "deezerArtist", url = "https://api.deezer.com/artist")
public interface DeezerArtistConnector {

    @GetMapping("/{artistId}/albums")
    AlbumListDTO getAlbumListByArtistId(@PathVariable("artistId") Long artistId) throws CustomBadRequestException;

}
