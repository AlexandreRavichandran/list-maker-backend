package com.medialistmaker.music.controller.deezerapi;

import com.medialistmaker.music.dto.externalapi.deezerapi.SongElementDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.search.list.SongSearchListDTO;
import com.medialistmaker.music.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.music.service.externalapi.deezer.SongApiServiceImpl;
import com.medialistmaker.music.service.externalapi.deezer.search.SongSearchApiServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/musics/deezerapi/songs")
public class SongController {

    @Autowired
    SongSearchApiServiceImpl songSearchApiService;

    @Autowired
    SongApiServiceImpl songApiService;

    @GetMapping
    public ResponseEntity<SongSearchListDTO> getBySongName(@RequestParam("name") String songName)
            throws CustomBadRequestException {

        return new ResponseEntity<>(
                this.songSearchApiService.getBySongName(songName),
                HttpStatus.OK
        );

    }

    @GetMapping("/{apicode}")
    public ResponseEntity<SongElementDTO> getByApiCode(@PathVariable("apicode") String apiCode) throws CustomBadRequestException {

        return new ResponseEntity<>(this.songApiService.getByApiCode(apiCode), HttpStatus.OK);

    }
}
