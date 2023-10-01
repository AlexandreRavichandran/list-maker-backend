package com.medialistmaker.list.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.medialistmaker.list.domain.MovieListItem;
import com.medialistmaker.list.dto.MovieListItemDTO;
import com.medialistmaker.list.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.list.exception.entityduplicationexception.CustomEntityDuplicationException;
import com.medialistmaker.list.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.list.service.movielistitem.MovieListItemServiceImpl;
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

@WebMvcTest(MovieListItemController.class)
class MovieListItemControllerTest {

    @Autowired
    MockMvc mockMvc;

    @SpyBean
    ModelMapper modelMapper;

    @MockBean
    MovieListItemServiceImpl movieItemServiceImpl;

    @Test
    void givenAppUserIdWhenGetByAppUserIdShouldReturnRelatedMovieListItemAndReturn200() throws Exception {

        MovieListItem firstMovieListItem = MovieListItem
                .builder()
                .movieId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        MovieListItem secondMovieListItem = MovieListItem
                .builder()
                .movieId(2L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(2)
                .build();

        MovieListItem thirdMovieListItem = MovieListItem
                .builder()
                .movieId(3L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(3)
                .build();

        List<MovieListItem> movieListItemList = List.of(firstMovieListItem, secondMovieListItem, thirdMovieListItem);

        Mockito.when(this.movieItemServiceImpl.getByAppUserId(anyLong())).thenReturn(movieListItemList);

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .get("/api/lists/movies")
                )
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$", hasSize(movieListItemList.size()))
                );
    }

    @Test
    void givenMovieListItemWhenAddMovieListItemShouldSaveAndReturnMovieListItemAndReturn201() throws Exception {

        MovieListItemDTO movieListItemDTO = MovieListItemDTO
                .builder()
                .movieId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        MovieListItem movieListItem = MovieListItem
                .builder()
                .movieId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        Mockito.when(this.movieItemServiceImpl.add(movieListItem)).thenReturn(movieListItem);

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .post("/api/lists/movies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        new ObjectMapper()
                                                .configure(
                                                        SerializationFeature.WRAP_ROOT_VALUE,
                                                        false)
                                                .writeValueAsString(movieListItemDTO)
                                )
                )
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.movieId", equalTo(movieListItem.getMovieId().intValue())),
                        jsonPath("$.appUserId", equalTo(movieListItem.getAppUserId().intValue()))
                );
    }

    @Test
    void givenInvalidMovieListItemWhenAddMovieListItemShouldReturnErrorListAndReturn400() throws Exception {

        MovieListItemDTO movieListItemDTO = MovieListItemDTO
                .builder()
                .movieId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        MovieListItem movieListItem = MovieListItem
                .builder()
                .movieId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        List<String> errorList = List.of("Error 1", "Error 2");

        Mockito
                .when(this.movieItemServiceImpl.add(movieListItem))
                .thenThrow(new CustomBadRequestException("Error", errorList));

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .post("/api/lists/movies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        new ObjectMapper()
                                                .configure(
                                                        SerializationFeature.WRAP_ROOT_VALUE,
                                                        false)
                                                .writeValueAsString(movieListItemDTO)
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
    void givenExistingMovieListItemWhenAddMovieListItemShouldReturn400() throws Exception {

        MovieListItemDTO movieListItemDTO = MovieListItemDTO
                .builder()
                .appUserId(1L)
                .movieId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        MovieListItem movieListItem = MovieListItem
                .builder()
                .appUserId(1L)
                .movieId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        Mockito
                .when(this.movieItemServiceImpl.add(movieListItem))
                .thenThrow(new CustomEntityDuplicationException("Already exists"));

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .post("/api/lists/movies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        new ObjectMapper()
                                                .configure(
                                                        SerializationFeature.WRAP_ROOT_VALUE,
                                                        false)
                                                .writeValueAsString(movieListItemDTO)
                                )
                )
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        result -> assertTrue(result.getResolvedException() instanceof CustomEntityDuplicationException)
                );
    }

    @Test
    void givenListItemIdWhenDeleteByIdShouldDeleteAndReturnRelatedListItemAndReturn200() throws Exception {

        MovieListItem movieListItem = MovieListItem
                .builder()
                .id(1L)
                .movieId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        Mockito.when(this.movieItemServiceImpl.deleteById(anyLong())).thenReturn(movieListItem);

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/api/lists/movies/{movieItemId}",
                                        movieListItem.getId()
                                )
                )
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id", equalTo(movieListItem.getId().intValue()))
                );

    }

    @Test
    void givenInvalidListItemIdWhenDeleteByIdShouldDeleteAndReturn404() throws Exception {

        Mockito.when(this.movieItemServiceImpl.deleteById(anyLong())).thenThrow(new CustomNotFoundException("Error"));

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/api/lists/movies/{musicId}",
                                        1L
                                )
                )
                .andDo(print())
                .andExpect(
                        status().isNotFound()
                );

    }
}