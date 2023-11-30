package com.medialistmaker.music.controller.deezerapi;

import com.medialistmaker.music.connector.deezer.artist.DeezerArtistConnectorProxy;
import com.medialistmaker.music.dto.externalapi.deezerapi.AlbumListDTO;
import com.medialistmaker.music.exception.badrequestexception.CustomBadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/musics/deezerapi/artists")
public class ArtistController {

    @Autowired
    DeezerArtistConnectorProxy deezerArtistConnectorProxy;

    @GetMapping("/{artistId}/albums")
    public ResponseEntity<AlbumListDTO> getAlbumListByArtistId(@PathVariable("artistId") Long artistId)
    throws CustomBadRequestException
    {

        return new ResponseEntity<>(
                this.deezerArtistConnectorProxy.getAlbumListByArtistId(artistId),
                HttpStatus.OK
        );
    }
}
