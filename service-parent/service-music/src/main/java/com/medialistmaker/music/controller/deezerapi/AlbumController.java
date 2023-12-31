package com.medialistmaker.music.controller.deezerapi;

import com.medialistmaker.music.connector.deezer.album.DeezerAlbumConnectorProxy;
import com.medialistmaker.music.connector.deezer.search.DeezerSearchConnectorProxy;
import com.medialistmaker.music.connector.list.ListConnectorProxy;
import com.medialistmaker.music.dto.externalapi.deezerapi.AlbumElementDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.SongElementDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.TrackListDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.search.list.AlbumSearchListDTO;
import com.medialistmaker.music.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.music.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.music.exception.servicenotavailableexception.ServiceNotAvailableException;
import com.medialistmaker.music.service.music.MusicServiceImpl;
import com.medialistmaker.music.utils.DeezerParameterFormatter;
import com.medialistmaker.music.utils.MathUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

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

    @Autowired
    DeezerParameterFormatter parameterFormatter;

    @GetMapping
    public ResponseEntity<AlbumSearchListDTO> browseByQueryAndFilter(
            @RequestParam("name") String albumName,
            @RequestParam(value = "artist", required = false) String artist,
            @RequestParam(value = "label", required = false) String label)
            throws CustomBadRequestException {

        Map<String, String> params = new HashMap<>();
        params.put("album", albumName);
        params.put("artist", artist);
        params.put("label", label);

        return new ResponseEntity<>(
                this.albumSearchConnectorProxy.getAlbumByQuery(this.parameterFormatter.formatParams(params)),
                HttpStatus.OK
        );

    }

    @GetMapping("/apicodes/{apicode}")
    public ResponseEntity<AlbumElementDTO> getByApiCode(@PathVariable("apicode") String apiCode)
            throws CustomBadRequestException, ServiceNotAvailableException, CustomNotFoundException {

        AlbumElementDTO albumElementDTO = this.albumConnectorProxy.getByApiCode(apiCode);

        if(isNull(albumElementDTO.getApiCode())) {
            throw new CustomNotFoundException("Movie not found");
        }

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
