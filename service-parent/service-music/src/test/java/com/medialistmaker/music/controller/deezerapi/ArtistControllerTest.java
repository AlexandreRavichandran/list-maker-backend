package com.medialistmaker.music.controller.deezerapi;

import com.medialistmaker.music.connector.deezer.artist.DeezerArtistConnectorProxy;
import com.medialistmaker.music.dto.externalapi.deezerapi.AlbumElementDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.AlbumListDTO;
import com.medialistmaker.music.exception.badrequestexception.CustomBadRequestException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArtistController.class)
class ArtistControllerTest {

    @MockBean
    DeezerArtistConnectorProxy deezerArtistConnectorProxy;

    @Autowired
    MockMvc mockMvc;

    @Test
    void givenArtistIdWhenGetAlbumListByArtistIdShouldReturnRelatedAlbumListAndReturn200() throws Exception {

        AlbumElementDTO firstAlbum = new AlbumElementDTO();
        firstAlbum.setApiCode("1");

        AlbumElementDTO secondAlbum = new AlbumElementDTO();
        secondAlbum.setApiCode("2");

        AlbumElementDTO thirdAlbum = new AlbumElementDTO();
        thirdAlbum.setApiCode("3");

        AlbumListDTO listDTO = new AlbumListDTO();
        listDTO.setAlbumList(List.of(firstAlbum, secondAlbum, thirdAlbum));

        Mockito.when(this.deezerArtistConnectorProxy.getAlbumListByArtistId(anyLong())).thenReturn(listDTO);

        this.mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/musics/deezerapi/artists/{artistId}/albums",
                                "1")
        )
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.albumList", hasSize(3))
                );
    }

    @Test
    void givenArtistIdWhenGetAlbumListByArtistIdAndApiNotAvailableShouldThrowBadRequestExceptionAndReturn400() throws Exception {

        Mockito
                .when(this.deezerArtistConnectorProxy.getAlbumListByArtistId(anyLong()))
                .thenThrow(CustomBadRequestException.class);

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/musics/deezerapi/artists/{artistId}/albums",
                                        "1")
                )
                .andDo(print())
                .andExpect(
                        status().isBadRequest()
                );

    }
}