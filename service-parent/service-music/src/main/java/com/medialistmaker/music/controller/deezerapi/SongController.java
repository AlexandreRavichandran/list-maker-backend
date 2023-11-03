package com.medialistmaker.music.controller.deezerapi;

import com.medialistmaker.music.connector.deezer.search.DeezerSearchConnectorProxy;
import com.medialistmaker.music.connector.deezer.song.DeezerSongConnector;
import com.medialistmaker.music.dto.externalapi.deezerapi.SongElementDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.search.list.SongSearchListDTO;
import com.medialistmaker.music.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.music.exception.servicenotavailableexception.ServiceNotAvailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/musics/deezerapi/songs")
public class SongController {

    @Autowired
    DeezerSearchConnectorProxy songConnectorProxy;

    @Autowired
    DeezerSongConnector deezerSongConnector;

    @GetMapping
    public ResponseEntity<SongSearchListDTO> getBySongName(@RequestParam("name") String songName)
            throws CustomBadRequestException {

        return new ResponseEntity<>(
                this.songConnectorProxy.getSongByQuery(songName),
                HttpStatus.OK
        );

    }

    @GetMapping("/apicodes/{apicode}")
    public ResponseEntity<SongElementDTO> getByApiCode(@PathVariable("apicode") String apiCode)
            throws CustomBadRequestException, ServiceNotAvailableException {

        return new ResponseEntity<>(this.deezerSongConnector.getByApiCode(apiCode), HttpStatus.OK);

    }
}
