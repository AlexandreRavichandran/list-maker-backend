package com.medialistmaker.music.controller.deezerapi;

import com.medialistmaker.music.connector.deezer.album.DeezerAlbumConnectorProxy;
import com.medialistmaker.music.connector.deezer.search.DeezerSearchConnectorProxy;
import com.medialistmaker.music.connector.list.ListConnectorProxy;
import com.medialistmaker.music.domain.Music;
import com.medialistmaker.music.dto.externalapi.deezerapi.AlbumElementDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.ArtistElementDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.SongElementDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.TrackListDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.search.item.AlbumSearchElementDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.search.list.AlbumSearchListDTO;
import com.medialistmaker.music.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.music.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.music.service.music.MusicServiceImpl;
import com.medialistmaker.music.utils.DeezerParameterFormatter;
import com.medialistmaker.music.utils.MathUtils;
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
    DeezerSearchConnectorProxy albumSearchConnectorProxy;

    @MockBean
    DeezerAlbumConnectorProxy albumConnectorProxy;

    @MockBean
    ListConnectorProxy listConnectorProxy;

    @MockBean
    MathUtils mathUtils;

    @MockBean
    DeezerParameterFormatter deezerFormatter;

    @MockBean
    MusicServiceImpl musicService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void givenAlbumNameWhenGetByAlbumNameShouldReturnAlbumSearchListAndReturn200() throws Exception {

        ArtistElementDTO artist = new ArtistElementDTO();
        artist.setApiCode("1");
        artist.setName("Artist");

        AlbumSearchElementDTO firstAlbum = new AlbumSearchElementDTO();
        firstAlbum.setApiCode("1L");
        firstAlbum.setTitle("Album 1");
        firstAlbum.setPictureUrl("test.com");
        firstAlbum.setArtist(artist);

        AlbumSearchElementDTO secondAlbum = new AlbumSearchElementDTO();
        secondAlbum.setApiCode("2L");
        secondAlbum.setTitle("Album 2");
        secondAlbum.setPictureUrl("test.com");
        secondAlbum.setArtist(artist);

        AlbumSearchListDTO albumSearchListDTO = new AlbumSearchListDTO();
        albumSearchListDTO.setSearchResults(List.of(firstAlbum, secondAlbum));

        Mockito.when(this.deezerFormatter.formatParams(anyMap())).thenReturn("query");

        Mockito
                .when(this.albumSearchConnectorProxy.getAlbumByQuery(anyString(), anyInt()))
                .thenReturn(albumSearchListDTO);

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(
                                        "/api/musics/deezerapi/albums"
                                )
                                .param("name", "test")
                                .param("artist", "artist")
                                .param("page", "0")
                )
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.searchResults", hasSize(2))
                );
    }


    @Test
    void givenNonExistingApiCodeWhenGetByApiCodeShouldReturnRelatedAlbumElementWithAlreadyInListFieldFalseAndReturn200() throws Exception {

        ArtistElementDTO artist = new ArtistElementDTO();
        artist.setApiCode("1");
        artist.setName("Artist");

        AlbumElementDTO album = new AlbumElementDTO();
        album.setApiCode("1");
        album.setTitle("Album");
        album.setPictureUrl("test.com");
        album.setArtist(artist);

        Mockito
                .when(this.albumConnectorProxy.getByApiCode(anyString()))
                .thenReturn(album);

        Mockito
                .when(this.musicService.readByApiCodeAndType(anyString(), anyInt()))
                .thenThrow(CustomNotFoundException.class);

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(
                                        "/api/musics/deezerapi/albums/apicodes/{apicode}",
                                        "test"
                                )
                                .param("type", "1")
                )
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.apiCode", equalTo(album.getApiCode()))
                );
    }

    @Test
    void givenApiCodeWhenGetByApiCodeShouldReturnRelatedAlbumElementAndReturn200() throws Exception {

        ArtistElementDTO artist = new ArtistElementDTO();
        artist.setApiCode("1");
        artist.setName("Artist");

        AlbumElementDTO album = new AlbumElementDTO();
        album.setApiCode("1");
        album.setTitle("Album");
        album.setPictureUrl("test.com");
        album.setArtist(artist);

        Music music = new Music();
        music.setId(1L);
        music.setApiCode("XXX");

        Mockito
                .when(this.albumConnectorProxy.getByApiCode(anyString()))
                .thenReturn(album);

        Mockito
                .when(this.musicService.readByApiCodeAndType(anyString(), anyInt()))
                .thenReturn(music);

        Mockito
                .when(this.listConnectorProxy.isMusicIdAlreadyInList(1L))
                .thenReturn(Boolean.TRUE);

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(
                                        "/api/musics/deezerapi/albums/apicodes/{apicode}",
                                        "test"
                                )
                                .param("type", "1")
                )
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.apiCode", equalTo(album.getApiCode()))
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
    void givenInvalidApiCodeWhenGetByApiCodeShouldThrowNotFoundExceptionAndReturn404() throws Exception {

        AlbumElementDTO album = new AlbumElementDTO();

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
                .andExpect(
                        status().isNotFound()
                );

    }

    @Test
    void givenApiCodeWhenGetTrackListByApiCodeShouldReturnRelatedSongElementListAndReturn200() throws Exception {

        SongElementDTO firstElement = new SongElementDTO();
        firstElement.setApiCode("1");
        firstElement.setDuration(180);
        firstElement.setRank(995000);

        SongElementDTO secondElement = new SongElementDTO();
        secondElement.setApiCode("2");
        secondElement.setDuration(180);
        secondElement.setRank(994000);

        SongElementDTO thirdElement = new SongElementDTO();
        thirdElement.setApiCode("3");
        thirdElement.setDuration(180);
        thirdElement.setRank(997000);

        TrackListDTO trackListDTO = new TrackListDTO();
        trackListDTO.setSongList(List.of(firstElement, secondElement, thirdElement));

        Mockito
                .when(this.albumConnectorProxy.getTrackListByAlbumApiCode(anyString()))
                .thenReturn(trackListDTO);

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
                        jsonPath("$.songList", hasSize(trackListDTO.getSongList().size()))
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