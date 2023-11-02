package com.medialistmaker.list.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.medialistmaker.list.domain.MovieListItem;
import com.medialistmaker.list.domain.MusicListItem;
import com.medialistmaker.list.dto.movie.MovieListItemAddDTO;
import com.medialistmaker.list.dto.music.MusicListItemAddDTO;
import com.medialistmaker.list.dto.music.MusicListItemDTO;
import com.medialistmaker.list.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.list.exception.entityduplicationexception.CustomEntityDuplicationException;
import com.medialistmaker.list.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.list.exception.servicenotavailableexception.ServiceNotAvailableException;
import com.medialistmaker.list.service.musiclistitem.MusicListItemServiceImpl;
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

import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MusicListItemController.class)
class MusicListItemControllerTest {

    @Autowired
    MockMvc mockMvc;

    @SpyBean
    ModelMapper modelMapper;

    @MockBean
    MusicListItemServiceImpl musicItemServiceImpl;

    @Test
    void givenAppUserIdWhenGetByAppUserIdShouldReturnRelatedMusicListItemAndReturn200() throws Exception {

        MusicListItem firstMusicListItem = MusicListItem
                .builder()
                .musicId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        MusicListItem secondMusicListItem = MusicListItem
                .builder()
                .musicId(2L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(2)
                .build();

        MusicListItem thirdMusicListItem = MusicListItem
                .builder()
                .musicId(3L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(3)
                .build();

        List<MusicListItem> musicListItemList = List.of(firstMusicListItem, secondMusicListItem, thirdMusicListItem);

        Mockito.when(this.musicItemServiceImpl.getByAppUserId(anyLong())).thenReturn(musicListItemList);

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .get("/api/lists/musics")
                )
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$", hasSize(musicListItemList.size()))
                );
    }

    @Test
    void givenMusicListItemAddWhenAddMusicListItemShouldSaveAndReturnMusicListItemAndReturn201() throws Exception {

        MusicListItem musicListItem = MusicListItem
                .builder()
                .musicId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        MusicListItemAddDTO musicListItemAddDTO = new MusicListItemAddDTO();
        musicListItemAddDTO.setApiCode("XXXXX");

        Mockito.when(this.musicItemServiceImpl.add(musicListItemAddDTO)).thenReturn(musicListItem);

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .post("/api/lists/musics")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        new ObjectMapper()
                                                .configure(
                                                        SerializationFeature.WRAP_ROOT_VALUE,
                                                        false)
                                                .writeValueAsString(musicListItemAddDTO)
                                )
                )
                .andDo(print())
                .andExpectAll(
                        status().isCreated()
                );
    }

    @Test
    void givenInvalidMusicListItemAddWhenAddMusicListItemShouldThrowBadRequestExceptionAndReturn400() throws Exception {

        MusicListItemAddDTO musicListItemAddDTO = new MusicListItemAddDTO();
        musicListItemAddDTO.setApiCode("XXXXX");

        Mockito.when(this.musicItemServiceImpl.add(musicListItemAddDTO)).thenThrow(CustomBadRequestException.class);

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .post("/api/lists/musics")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        new ObjectMapper()
                                                .configure(
                                                        SerializationFeature.WRAP_ROOT_VALUE,
                                                        false)
                                                .writeValueAsString(musicListItemAddDTO)
                                )
                )
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest()
                );
    }

    @Test
    void givenExistingMusicListItemAddWhenAddMusicListItemShouldThrowEntityDuplicationExceptionAndReturn409() throws Exception {

        MusicListItemAddDTO musicListItemAddDTO = new MusicListItemAddDTO();
        musicListItemAddDTO.setApiCode("XXXXX");

        Mockito.when(this.musicItemServiceImpl.add(musicListItemAddDTO)).thenThrow(CustomEntityDuplicationException.class);

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .post("/api/lists/musics")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        new ObjectMapper()
                                                .configure(
                                                        SerializationFeature.WRAP_ROOT_VALUE,
                                                        false)
                                                .writeValueAsString(musicListItemAddDTO)
                                )
                )
                .andDo(print())
                .andExpectAll(
                        status().isConflict()
                );
    }

    @Test
    void givenMusicListItemAddWhenAddMusicListItemAndServiceNotAvailableShouldThrowServiceNotAvailableExceptionAndReturn424() throws Exception {

        MusicListItemAddDTO musicListItemAddDTO = new MusicListItemAddDTO();
        musicListItemAddDTO.setApiCode("XXXXX");

        Mockito.when(this.musicItemServiceImpl.add(musicListItemAddDTO)).thenThrow(ServiceNotAvailableException.class);

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .post("/api/lists/musics")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        new ObjectMapper()
                                                .configure(
                                                        SerializationFeature.WRAP_ROOT_VALUE,
                                                        false)
                                                .writeValueAsString(musicListItemAddDTO)
                                )
                )
                .andDo(print())
                .andExpectAll(
                        status().isFailedDependency()
                );
    }


    @Test
    void givenListItemIdWhenDeleteByIdShouldDeleteAndReturnRelatedListItemAndReturn200() throws Exception {

        MusicListItem musicListItem = MusicListItem
                .builder()
                .id(1L)
                .musicId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        Mockito.when(this.musicItemServiceImpl.deleteById(anyLong())).thenReturn(musicListItem);

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/api/lists/musics/{musicItemId}",
                                        musicListItem.getId()
                                )
                )
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id", equalTo(musicListItem.getId().intValue()))
                );

    }

    @Test
    void givenInvalidListItemIdWhenDeleteByIdShouldDeleteAndReturn404() throws Exception {

        Mockito.when(this.musicItemServiceImpl.deleteById(anyLong())).thenThrow(new CustomNotFoundException("Error"));

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/api/lists/musics/{musicId}",
                                        1L
                                )
                )
                .andDo(print())
                .andExpect(
                        status().isNotFound()
                );

    }
}