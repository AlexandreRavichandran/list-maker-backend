package com.medialistmaker.music.controller.deezerapi;

import com.medialistmaker.music.connector.deezer.album.DeezerAlbumConnectorProxy;
import com.medialistmaker.music.connector.deezer.search.DeezerSearchConnectorProxy;
import com.medialistmaker.music.connector.list.ListConnectorProxy;
import com.medialistmaker.music.constant.MusicTypeConstant;
import com.medialistmaker.music.domain.Music;
import com.medialistmaker.music.dto.externalapi.deezerapi.AlbumElementDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.SongElementDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.TrackListDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.search.list.AlbumSearchListDTO;
import com.medialistmaker.music.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.music.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.music.exception.servicenotavailableexception.ServiceNotAvailableException;
import com.medialistmaker.music.service.music.MusicServiceImpl;
import com.medialistmaker.music.utils.MathUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/musics/deezerapi/albums")
public class AlbumController {

    @Autowired
    DeezerSearchConnectorProxy albumSearchConnectorProxy;

    @Autowired
    DeezerAlbumConnectorProxy albumConnectorProxy;

    @Autowired
    ListConnectorProxy listConnectorProxy;

    @Autowired
    MathUtils mathUtils;

    @Autowired
    MusicServiceImpl musicService;

    @GetMapping
    public ResponseEntity<AlbumSearchListDTO> getByAlbumName(@RequestParam("name") String albumName)
            throws CustomBadRequestException {

        return new ResponseEntity<>(
                this.albumSearchConnectorProxy.getAlbumByQuery(albumName),
                HttpStatus.OK
        );

    }

    @GetMapping("/apicodes/{apicode}")
    public ResponseEntity<AlbumElementDTO> getByApiCode(@PathVariable("apicode") String apiCode)
            throws CustomBadRequestException, ServiceNotAvailableException {

        Boolean isAlreadyInList;

        AlbumElementDTO albumElementDTO = this.albumConnectorProxy.getByApiCode(apiCode);

        try {

            Music music = this.musicService.readByApiCodeAndType(albumElementDTO.getApiCode(), MusicTypeConstant.TYPE_ALBUM);

            isAlreadyInList = this.listConnectorProxy.isMusicIdAlreadyInList(music.getId());

        } catch (CustomNotFoundException e) {
            isAlreadyInList = Boolean.FALSE;
        }

        albumElementDTO.setIsAlreadyInList(isAlreadyInList);

        return new ResponseEntity<>(albumElementDTO, HttpStatus.OK);

    }

    @GetMapping("/apicodes/{apicode}/tracklist")
    public ResponseEntity<TrackListDTO> getTrackListByAlbumApiCode(@PathVariable("apicode") String apiCode)
            throws ServiceNotAvailableException, CustomBadRequestException {

        TrackListDTO trackListDTO = this.albumConnectorProxy.getTrackListByAlbumApiCode(apiCode);

        trackListDTO.getSongList().forEach(songElementDTO -> songElementDTO.setDuration(songElementDTO.getDuration() * 1000));

        int totalSongDurationInSeconds = trackListDTO.getSongList().stream().mapToInt(SongElementDTO::getDuration).sum();

        Integer averageAlbumPopularity = this.mathUtils.calculateAverageOfList(trackListDTO.getSongList().stream().mapToInt(SongElementDTO::getRank).toArray());

        trackListDTO.setTotalDurationInEpochMilli((long) totalSongDurationInSeconds);
        trackListDTO.setAlbumPopularityRate(((averageAlbumPopularity/1000000D) * 100));

        return new ResponseEntity<>(trackListDTO, HttpStatus.OK);
    }
}
