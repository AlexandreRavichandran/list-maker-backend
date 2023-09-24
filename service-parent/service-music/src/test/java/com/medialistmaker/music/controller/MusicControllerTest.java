package com.medialistmaker.music.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.medialistmaker.music.domain.Music;
import com.medialistmaker.music.dto.MusicDTO;
import com.medialistmaker.music.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.music.exception.entityduplicationexception.CustomEntityDuplicationException;
import com.medialistmaker.music.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.music.service.music.MusicServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MusicController.class)
class MusicControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MusicServiceImpl musicService;

    @SpyBean
    ModelMapper modelMapper;

    @Test
    void givenIdListWhenBrowseByIdsShouldReturnRelatedMusicDTOListAndReturn200() throws Exception {

        Music firstMusic = Music
                .builder()
                .title("First music")
                .artistName("Artist 1")
                .id(1L)
                .type(1)
                .apiCode("MUSIC1")
                .pictureUrl("http://test.jpg")
                .releasedAt(2000)
                .build();

        Music secondMusic = Music
                .builder()
                .title("Second music")
                .id(1L)
                .artistName("Artist 2")
                .type(1)
                .apiCode("MUSIC2")
                .pictureUrl("http://test.jpg")
                .releasedAt(2002)
                .build();

        List<Music> musicList = List.of(firstMusic, secondMusic);

        Mockito.when(this.musicService.browseByIds(anyList())).thenReturn(musicList);

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/musics")
                                .param("musicIds", "1")
                                .param("musicIds", "2")
                )
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$", hasSize(musicList.size()))
                );

    }

    @Test
    void givenTypeWhenBrowseByTypeShouldReturnRelatedMusicDTOListAndReturn200() throws Exception {

        Music firstMusic = Music
                .builder()
                .title("First music")
                .artistName("Artist 1")
                .id(1L)
                .type(1)
                .apiCode("MUSIC1")
                .pictureUrl("http://test.jpg")
                .releasedAt(2000)
                .build();

        Music secondMusic = Music
                .builder()
                .title("Second music")
                .id(1L)
                .artistName("Artist 2")
                .type(1)
                .apiCode("MUSIC2")
                .pictureUrl("http://test.jpg")
                .releasedAt(2002)
                .build();

        List<Music> musicList = List.of(firstMusic, secondMusic);

        Mockito.when(this.musicService.browseByType(anyInt())).thenReturn(musicList);

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/musics/types/{type}",
                                        "1")
                )
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$", hasSize(musicList.size()))
                );
    }

    @Test
    void givenIdWhenReadByIdShouldReturnRelatedMusicDTOAndReturn200() throws Exception {

        Music music = Music
                .builder()
                .id(1L)
                .title("Music")
                .artistName("Artist")
                .type(1)
                .apiCode("MUSIC")
                .pictureUrl("http://test.jpg")
                .releasedAt(2000)
                .build();

        Mockito.when(this.musicService.readById(anyLong())).thenReturn(music);

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/musics/{musicId}",
                                        "1")
                )
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id", equalTo(music.getId().intValue()))
                );

    }

    @Test
    void givenInvalidIdWhenReadByIdShouldReturn404() throws Exception {

        Mockito.when(this.musicService.readById(anyLong())).thenThrow(new CustomNotFoundException("Error"));

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/musics/{musicId}",
                                        "1")
                )
                .andDo(print())
                .andExpect(
                        status().isNotFound()
                );

    }

    @Test
    void givenApiCodeWhenReadByApiCodeShouldReturnRelatedMusicDTOAndReturn200() throws Exception {

        Music music = Music
                .builder()
                .id(1L)
                .title("Music")
                .artistName("Artist")
                .type(1)
                .apiCode("MUSIC")
                .pictureUrl("http://test.jpg")
                .releasedAt(2000)
                .build();

        Mockito.when(this.musicService.readByApiCode(anyString())).thenReturn(music);

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/musics/apicode/{apicode}",
                                        "MUSIC")
                )
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id", equalTo(music.getId().intValue()))
                );

    }

    @Test
    void givenInvalidApiCodeWhenReadByApiCodeShouldReturn404() throws Exception {

        Mockito.when(this.musicService.readByApiCode(anyString())).thenThrow(new CustomNotFoundException("Error"));

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/musics/apicode/{apicode}",
                                        "test")
                )
                .andDo(print())
                .andExpect(
                        status().isNotFound()
                );

    }

    @Test
    void givenMusicWhenAddMusicShouldSaveAndReturnMusicDTOAndReturn201() throws Exception {

        MusicDTO musicDTO = MusicDTO
                .builder()
                .title("Music")
                .artistName("Artist")
                .type(1)
                .apiCode("MUSIC")
                .pictureUrl("http://test.jpg")
                .releasedAt(2000)
                .build();

        Music music = Music
                .builder()
                .title("Music")
                .artistName("Artist")
                .type(1)
                .apiCode("MUSIC")
                .pictureUrl("http://test.jpg")
                .releasedAt(2000)
                .build();

        Mockito.when(this.musicService.add(music)).thenReturn(music);

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/musics")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        new ObjectMapper()
                                                .configure(
                                                        SerializationFeature.WRAP_ROOT_VALUE,
                                                        false)
                                                .writeValueAsString(musicDTO)
                                )
                )
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.apiCode", equalTo(music.getApiCode()))
                );
    }

    @Test
    void givenInvalidMusicWhenAddMusicShouldReturnListOfErrorAndReturn400() throws Exception {

        MusicDTO musicDTO = MusicDTO
                .builder()
                .title("Music")
                .artistName("Artist")
                .type(1)
                .apiCode("MUSIC")
                .pictureUrl("http://test.jpg")
                .releasedAt(2000)
                .build();

        Music music = Music
                .builder()
                .title("Music")
                .artistName("Artist")
                .type(1)
                .apiCode("MUSIC")
                .pictureUrl("http://test.jpg")
                .releasedAt(2000)
                .build();

        List<String> errorList = List.of("Error 1", "Error 2");

        Mockito.when(this.musicService.add(music)).thenThrow(new CustomBadRequestException("Error", errorList));

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/musics")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        new ObjectMapper()
                                                .configure(
                                                        SerializationFeature.WRAP_ROOT_VALUE,
                                                        false)
                                                .writeValueAsString(musicDTO)
                                )
                )
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        result -> assertTrue(result.getResolvedException() instanceof CustomBadRequestException),
                        result -> assertEquals(errorList.size(),
                                ((CustomBadRequestException) result.getResolvedException()).getErrorList().size()
                        )

                );
    }

    @Test
    void givenMusicWithExistingApiCodeWhenAddMusicShouldReturn400() throws Exception {

        MusicDTO musicDTO = MusicDTO
                .builder()
                .title("Music")
                .artistName("Artist")
                .id(1L)
                .type(1)
                .apiCode("MUSIC")
                .pictureUrl("http://test.jpg")
                .releasedAt(2000)
                .build();

        Music music = Music
                .builder()
                .title("Music")
                .artistName("Artist")
                .id(1L)
                .type(1)
                .apiCode("MUSIC")
                .pictureUrl("http://test.jpg")
                .releasedAt(2000)
                .build();

        Mockito.when(this.musicService.add(music)).thenThrow(new CustomEntityDuplicationException("Error"));

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/musics")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        new ObjectMapper()
                                                .configure(
                                                        SerializationFeature.WRAP_ROOT_VALUE,
                                                        false)
                                                .writeValueAsString(musicDTO)
                                )
                )
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        result -> assertTrue(result.getResolvedException() instanceof CustomEntityDuplicationException)
                );

    }

    @Test
    void givenIdWhenDeleteByIdShouldDeleteAndReturnRelatedMusicDTOAndReturn200() throws Exception {

        Music music = Music
                .builder()
                .id(1L)
                .title("Music")
                .artistName("Artist")
                .type(1)
                .apiCode("MUSIC")
                .pictureUrl("http://test.jpg")
                .releasedAt(2000)
                .build();

        Mockito.when(this.musicService.deleteById(anyLong())).thenReturn(music);

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/api/musics/{musicId}",
                                        music.getId()
                                )
                )
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id", equalTo(music.getId().intValue()))
                );
    }

    @Test
    void givenInvalidIdWhenDeleteByIdShouldReturn404() throws Exception {

        Mockito.when(this.musicService.deleteById(anyLong())).thenThrow(new CustomNotFoundException("Error"));

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/api/musics/{musicId}",
                                        1L
                                )
                )
                .andDo(print())
                .andExpect(
                        status().isNotFound()
                );

    }
}