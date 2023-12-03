package com.medialistmaker.movie.controller;

import com.medialistmaker.movie.connector.list.ListConnectorProxy;
import com.medialistmaker.movie.connector.omdb.OmdbConnectorProxy;
import com.medialistmaker.movie.domain.Movie;
import com.medialistmaker.movie.dto.externalapi.omdbapi.collection.MovieElementListDTO;
import com.medialistmaker.movie.dto.externalapi.omdbapi.collection.MovieElementListItemDTO;
import com.medialistmaker.movie.dto.externalapi.omdbapi.item.MovieElementDTO;
import com.medialistmaker.movie.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.movie.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.movie.service.movie.MovieServiceImpl;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OmdbApiController.class)
class OmdbApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OmdbConnectorProxy omdbConnectorProxy;

    @MockBean
    ListConnectorProxy listConnectorProxy;

    @MockBean
    MovieServiceImpl movieService;

    @Test
    void givenMovieNameWhenGetByMovieNameShouldReturnRelatedMovieListAndReturn200() throws Exception {

        MovieElementListItemDTO firstListItemDTO = new MovieElementListItemDTO();
        firstListItemDTO.setApiCode("00001");
        firstListItemDTO.setPictureUrl("http://picture.com");
        firstListItemDTO.setTitle("Movie 1");
        firstListItemDTO.setReleasedAt("2000");

        MovieElementListItemDTO secondListItemDTO = new MovieElementListItemDTO();
        secondListItemDTO.setApiCode("00002");
        secondListItemDTO.setPictureUrl("http://picture.com");
        secondListItemDTO.setTitle("Movie 2");
        secondListItemDTO.setReleasedAt("2001");


        MovieElementListDTO listDTO = new MovieElementListDTO();
        listDTO.setResponseStatus("OK");
        listDTO.setTotalResults("2");
        listDTO.setMovieElementList(List.of(firstListItemDTO, secondListItemDTO));

        Mockito
                .when(this.omdbConnectorProxy.getByQuery(anyString()))
                .thenReturn(listDTO);

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(
                                        "/api/movies/omdbapi/names/{name}",
                                        "test"
                                )
                )
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.totalResults", equalTo(listDTO.getTotalResults())),
                        jsonPath("$.responseStatus", equalTo(listDTO.getResponseStatus())),
                        jsonPath("$.movieElementList", hasSize(2))
                );
    }

    @Test
    void givenMovieNameWhenGetByMovieNameAndApiNotAvailableShouldReturn400() throws Exception {

        Mockito
                .when(this.omdbConnectorProxy.getByQuery(anyString()))
                .thenThrow(new CustomBadRequestException("Test", new ArrayList<>()));

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(
                                        "/api/movies/omdbapi/names/{name}",
                                        "test"
                                )
                )
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest()
                );

    }

    @Test
    void givenApiCodeWhenGetByApiCodeShouldReturnRelatedMovieAndReturn200() throws Exception {

        MovieElementDTO movieElement = new MovieElementDTO();
        movieElement.setApiCode("00001");
        movieElement.setTitle("Movie 1");
        movieElement.setDirector("Alexandre");
        movieElement.setDuration("200m");
        movieElement.setSynopsis("Summary");
        movieElement.setReleasedAt("2000");

        Movie movie = new Movie();
        movie.setApiCode("XXXX");
        movie.setId(1L);

        Mockito
                .when(this.omdbConnectorProxy.getByApiCode(anyString()))
                .thenReturn(movieElement);

        Mockito
                .when(this.movieService.readByApiCode(anyString()))
                .thenReturn(movie);

        Mockito
                .when(this.listConnectorProxy.isMovieIdAlreadyInList(anyLong()))
                .thenReturn(Boolean.TRUE);

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(
                                        "/api/movies/omdbapi/apicodes/{apicode}",
                                        "test"
                                )
                )
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.apiCode", equalTo(movieElement.getApiCode())),
                        jsonPath("$.isAlreadyInList", equalTo(Boolean.TRUE))
                );
    }

    @Test
    void givenNonExistingApiCodeWhenGetByApiCodeShouldReturnRelatedMovieWithAlreadyInListFieldFalseAndReturn200() throws Exception {

        MovieElementDTO movieElement = new MovieElementDTO();
        movieElement.setApiCode("00001");
        movieElement.setTitle("Movie 1");
        movieElement.setDirector("Alexandre");
        movieElement.setDuration("200m");
        movieElement.setSynopsis("Summary");
        movieElement.setReleasedAt("2000");

        Mockito
                .when(this.omdbConnectorProxy.getByApiCode(anyString()))
                .thenReturn(movieElement);

        Mockito
                .when(this.movieService.readByApiCode(anyString()))
                .thenThrow(CustomNotFoundException.class);

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(
                                        "/api/movies/omdbapi/apicodes/{apicode}",
                                        "test"
                                )
                )
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.apiCode", equalTo(movieElement.getApiCode()))
                );
    }

    @Test
    void givenApiCodeWhenGetByApiCodeAndAPiNotAvailableShouldReturn400() throws Exception {

        Mockito
                .when(this.omdbConnectorProxy.getByApiCode(anyString()))
                .thenThrow(new CustomBadRequestException("Test", new ArrayList<>()));

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(
                                        "/api/movies/omdbapi/apicodes/{apicode}",
                                        "test"
                                )
                )
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest()
                );
    }
}