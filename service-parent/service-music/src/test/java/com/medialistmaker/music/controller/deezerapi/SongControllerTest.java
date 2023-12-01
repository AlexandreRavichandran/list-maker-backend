package com.medialistmaker.music.controller.deezerapi;

import com.medialistmaker.music.connector.deezer.search.DeezerSearchConnectorProxy;
import com.medialistmaker.music.connector.deezer.song.DeezerSongConnector;
import com.medialistmaker.music.dto.externalapi.deezerapi.ArtistElementDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.SongElementDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.search.item.SongSearchElementDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.search.list.SongSearchListDTO;
import com.medialistmaker.music.exception.badrequestexception.CustomBadRequestException;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SongController.class)
class SongControllerTest {

    @MockBean
    DeezerSearchConnectorProxy searchConnectorProxy;

    @MockBean
    DeezerSongConnector songConnector;

    @Autowired
    MockMvc mockMvc;

    @Test
    void givenSongNameWhenGetBySongNameShouldReturnSongSearchListAndReturn200() throws Exception {

        ArtistElementDTO artist = new ArtistElementDTO();
        artist.setId("1");
        artist.setName("Artist");

        SongSearchElementDTO firstSong = new SongSearchElementDTO();
        firstSong.setId("1L");
        firstSong.setTitle("Song 1");
        firstSong.setDuration("2m30");
        firstSong.setPreview("test.mp3");
        firstSong.setArtist(artist);

        SongSearchElementDTO secondSong = new SongSearchElementDTO();
        secondSong.setId("2L");
        secondSong.setTitle("Song 2");
        secondSong.setDuration("2m30");
        secondSong.setPreview("test.mp3");
        secondSong.setArtist(artist);

        SongSearchListDTO songSearchListDTO = new SongSearchListDTO();
        songSearchListDTO.setData(List.of(firstSong, secondSong));

        Mockito
                .when(this.searchConnectorProxy.getSongByQuery(anyString()))
                .thenReturn(songSearchListDTO);

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(
                                        "/api/musics/deezerapi/songs"
                                )
                                .param("name", "test")
                )
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data", hasSize(2))
                );

    }

    @Test
    void givenSongNameWhenGetBySongNameAndApiErrorShouldThrowBadRequestExceptionAndReturn400() throws Exception {

        Mockito
                .when(this.searchConnectorProxy.getSongByQuery(anyString()))
                .thenThrow(new CustomBadRequestException("Bad request", new ArrayList<>()));

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(
                                        "/api/musics/deezerapi/songs"
                                )
                                .param("name", "test")
                )
                .andExpect(
                        status().isBadRequest()
                );
    }

    @Test
    void givenApiCodeWhenGetByApiCodeShouldReturnRelatedSongElementAndReturn200() throws Exception {

        ArtistElementDTO artist = new ArtistElementDTO();
        artist.setId("1");
        artist.setName("Artist");

        SongElementDTO song = new SongElementDTO();
        song.setId("2L");
        song.setTitle("Song 2");
        song.setDuration(180);
        song.setPreview("test.mp3");
        song.setArtist(artist);

        Mockito
                .when(this.songConnector.getByApiCode(anyString()))
                .thenReturn(song);

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(
                                        "/api/musics/deezerapi/songs/apicodes/{apicode}",
                                        "test"
                                )
                )
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id", equalTo(song.getId()))
                );
    }

    @Test
    void givenApiCodeWhenGetByApiCodeAndApiErrorShouldThrowBadRequestExceptionAndReturn400() throws Exception {

        Mockito
                .when(this.songConnector.getByApiCode(anyString()))
                .thenThrow(new CustomBadRequestException("Bad request", new ArrayList<>()));

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(
                                        "/api/musics/deezerapi/songs/apicodes/{apicode}",
                                        "test"
                                )
                )
                .andExpect(
                        status().isBadRequest()
                );

    }
}