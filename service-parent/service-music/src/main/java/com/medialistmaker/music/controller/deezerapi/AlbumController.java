package com.medialistmaker.music.controller.deezerapi;

import com.medialistmaker.music.dto.externalapi.deezerapi.AlbumElementDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.search.list.AlbumSearchListDTO;
import com.medialistmaker.music.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.music.service.externalapi.deezer.AlbumApiServiceImpl;
import com.medialistmaker.music.service.externalapi.deezer.search.AlbumSearchApiServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/musics/deezerapi/albums")
public class AlbumController {

    @Autowired
    AlbumSearchApiServiceImpl albumSearchApiService;

    @Autowired
    AlbumApiServiceImpl albumApiService;

    @GetMapping
    public ResponseEntity<AlbumSearchListDTO> getByAlbumName(@RequestParam("name") String albumName)
            throws CustomBadRequestException {

        return new ResponseEntity<>(
                this.albumSearchApiService.getByAlbumName(albumName),
                HttpStatus.OK
        );

    }

    @GetMapping("/{apicode}")
    public ResponseEntity<AlbumElementDTO> getByApiCode(@PathVariable("apicode") String apiCode)
            throws CustomBadRequestException {

        return new ResponseEntity<>(this.albumApiService.getByApiCode(apiCode), HttpStatus.OK);

    }
}
