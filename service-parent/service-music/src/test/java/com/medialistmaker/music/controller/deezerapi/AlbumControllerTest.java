package com.medialistmaker.music.controller.deezerapi;

import com.medialistmaker.music.connector.deezer.album.DeezerAlbumConnectorProxy;
import com.medialistmaker.music.connector.deezer.search.DeezerSearchConnectorProxy;
import com.medialistmaker.music.dto.externalapi.deezerapi.AlbumElementDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.ArtistElementDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.SongElementDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.TrackListDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.search.item.AlbumSearchElementDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.search.list.AlbumSearchListDTO;
import com.medialistmaker.music.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.music.utils.MathUtils;
import com.medialistmaker.music.utils.TimeCalculator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AlbumController.class)
class AlbumControllerTest {

    @MockBean
    DeezerSearchConnectorProxy searchConnectorProxy;

    @MockBean
    DeezerAlbumConnectorProxy albumConnectorProxy;

    @MockBean
    MathUtils mathUtils;

    @MockBean
    TimeCalculator timeCalculator;

    @Autowired
    MockMvc mockMvc;

    @Test
    void givenAlbumNameWhenGetByAlbumNameShouldReturnAlbumSearchListAndReturn200() throws Exception {

        ArtistElementDTO artist = new ArtistElementDTO();
        artist.setId("1");
        artist.setName("Artist");

        AlbumSearchElementDTO firstAlbum = new AlbumSearchElementDTO();
        firstAlbum.setId("1L");
        firstAlbum.setTitle("Album 1");
        firstAlbum.setPictureUrl("test.com");
        firstAlbum.setArtist(artist);

        AlbumSearchElementDTO secondAlbum = new AlbumSearchElementDTO();
        secondAlbum.setId("2L");
        secondAlbum.setTitle("Album 2");
        secondAlbum.setPictureUrl("test.com");
        secondAlbum.setArtist(artist);

        AlbumSearchListDTO albumSearchListDTO = new AlbumSearchListDTO();
        albumSearchListDTO.setData(List.of(firstAlbum, secondAlbum));

        Mockito
                .when(this.searchConnectorProxy.getAlbumByQuery(anyString()))
                .thenReturn(albumSearchListDTO);

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(
                                        "/api/musics/deezerapi/albums"
                                )
                                .param("name", "test")
                )
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data", hasSize(2))
                );
    }

    @Test
    void givenAlbumNameWhenGetByAlbumNameAndApiErrorShouldThrowBadRequestExceptionAndReturn400() throws Exception {

        Mockito
                .when(this.searchConnectorProxy.getAlbumByQuery(anyString()))
                .thenThrow(new CustomBadRequestException("Bad request", new ArrayList<>()));

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(
                                        "/api/musics/deezerapi/albums"
                                )
                                .param("name", "test")
                )
                .andExpect(
                        status().isBadRequest()
                );

    }

    @Test
    void givenApiCodeWhenGetByApiCodeShouldReturnRelatedAlbumElementAndReturn200() throws Exception {

        ArtistElementDTO artist = new ArtistElementDTO();
        artist.setId("1");
        artist.setName("Artist");

        AlbumElementDTO album = new AlbumElementDTO();
        album.setId("1");
        album.setTitle("Album");
        album.setPictureUrl("test.com");
        album.setArtist(artist);

        Mockito
                .when(this.albumConnectorProxy.getByApiCode(anyString()))
                .thenReturn(album);

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(
                                        "/api/musics/deezerapi/albums/apicodes/{apicode}",
                                        "test"
                                )
                )
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id", equalTo(album.getId()))
                );
    }

    @Test
    void givenApiCodeWhenGetByApiCodeAndApiErrorShouldThrowBadRequestExceptionAndReturn400() throws Exception {

        Mockito
                .when(this.albumConnectorProxy.getByApiCode(anyString()))
                .thenThrow(new CustomBadRequestException("Bad request", new ArrayList<>()));

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(
                                        "/api/musics/deezerapi/albums/apicodes/{apicode}",
                                        "test"
                                )
                )
                .andExpect(
                        status().isBadRequest()
                );

    }

    @Test
    void givenApiCodeWhenGetTrackListByApiCodeShouldReturnRelatedSongElementListAndReturn200() throws Exception {

        SongElementDTO firstElement = new SongElementDTO();
        firstElement.setId("1");
        firstElement.setDuration(180);
        firstElement.setRank(995000);

        SongElementDTO secondElement = new SongElementDTO();
        secondElement.setId("2");
        secondElement.setDuration(180);
        secondElement.setRank(994000);

        SongElementDTO thirdElement = new SongElementDTO();
        thirdElement.setId("3");
        thirdElement.setDuration(180);
        thirdElement.setRank(997000);

        TrackListDTO trackListDTO = new TrackListDTO();
        trackListDTO.setSongs(List.of(firstElement, secondElement, thirdElement));

        Mockito
                .when(this.albumConnectorProxy.getTrackListByAlbumApiCode(anyString()))
                .thenReturn(trackListDTO);

        Mockito.when(this.timeCalculator.formatSecondsToHourMinutesAndSeconds(anyInt())).thenReturn("2m30");
        Mockito.when(this.mathUtils.calculateAverageOfList(any())).thenReturn(40);

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(
                                        "/api/musics/deezerapi/albums/apicodes/{apicode}/tracklist",
                                        "test"
                                )
                )
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data", hasSize(trackListDTO.getSongs().size()))
                );
    }

    @Test
    void givenApiCodeWhenGetTrackListByApiCodeAndApiErrorShouldThrowBadRequestExceptionAndReturn400() throws Exception {

        Mockito
                .when(this.albumConnectorProxy.getTrackListByAlbumApiCode(anyString()))
                .thenThrow(new CustomBadRequestException("Bad request", new ArrayList<>()));

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(
                                        "/api/musics/deezerapi/albums/apicodes/{apicode}/tracklist",
                                        "test"
                                )
                )
                .andExpect(
                        status().isBadRequest()
                );

    }
}