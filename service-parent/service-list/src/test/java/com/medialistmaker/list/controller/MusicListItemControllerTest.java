package com.medialistmaker.list.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.medialistmaker.list.domain.MusicListItem;
import com.medialistmaker.list.dto.MusicListItemDTO;
import com.medialistmaker.list.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.list.exception.entityduplicationexception.CustomEntityDuplicationException;
import com.medialistmaker.list.exception.notfoundexception.CustomNotFoundException;
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
    void givenMusicListItemWhenAddMusicListItemShouldSaveAndReturnMusicListItemAndReturn201() throws Exception {

        MusicListItemDTO musicListItemDTO = MusicListItemDTO
                .builder()
                .musicId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        MusicListItem musicListItem = MusicListItem
                .builder()
                .musicId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        Mockito.when(this.musicItemServiceImpl.add(musicListItem)).thenReturn(musicListItem);

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
                                                .writeValueAsString(musicListItemDTO)
                                )
                )
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.musicId", equalTo(musicListItem.getMusicId().intValue())),
                        jsonPath("$.appUserId", equalTo(musicListItem.getAppUserId().intValue()))
                );
    }

    @Test
    void givenInvalidMusicListItemWhenAddMusicListItemShouldReturnErrorListAndReturn400() throws Exception {

        MusicListItemDTO musicListItemDTO = MusicListItemDTO
                .builder()
                .musicId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        MusicListItem musicListItem = MusicListItem
                .builder()
                .musicId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        List<String> errorList = List.of("Error 1", "Error 2");

        Mockito
                .when(this.musicItemServiceImpl.add(musicListItem))
                .thenThrow(new CustomBadRequestException("Error", errorList));

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
                                                .writeValueAsString(musicListItemDTO)
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
    void givenExistingMusicListItemWhenAddMusicListItemShouldReturn400() throws Exception {

        MusicListItemDTO musicListItemDTO = MusicListItemDTO
                .builder()
                .musicId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        MusicListItem musicListItem = MusicListItem
                .builder()
                .musicId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        Mockito
                .when(this.musicItemServiceImpl.add(musicListItem))
                .thenThrow(new CustomEntityDuplicationException("Already exists"));

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
                                                .writeValueAsString(musicListItemDTO)
                                )
                )
                .andExpectAll(
                        status().isBadRequest(),
                        result -> assertTrue(result.getResolvedException() instanceof CustomEntityDuplicationException)
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