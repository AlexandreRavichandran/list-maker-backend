package com.medialistmaker.movie.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.medialistmaker.movie.domain.Movie;
import com.medialistmaker.movie.dto.MovieAddDTO;
import com.medialistmaker.movie.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.movie.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.movie.exception.servicenotavailableexception.ServiceNotAvailableException;
import com.medialistmaker.movie.service.movie.MovieServiceImpl;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovieController.class)
class MovieControllerTest {

    @Autowired
    MockMvc mockMvc;

    @SpyBean
    ModelMapper modelMapper;

    @MockBean
    MovieServiceImpl movieService;

    @Test
    void givenIdListWhenBrowseByIdsShouldReturnRelatedMovieDTOListAndReturn200() throws Exception {

        Movie firstMovie = Movie
                .builder()
                .id(1L)
                .title("First movie")
                .releasedAt(2000)
                .pictureUrl("http://movie1.jpg")
                .apiCode("MOVIE1")
                .build();

        Movie secondMovie = Movie
                .builder()
                .id(2L)
                .title("Second movie")
                .releasedAt(2002)
                .pictureUrl("http://movie2.jpg")
                .apiCode("MOVIE2")
                .build();

        Mockito.when(this.movieService.browseByIds(anyList())).thenReturn(List.of(firstMovie, secondMovie));

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/movies")
                                .param("movieIds", "1")
                                .param("movieIds", "2")
                )
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$", hasSize(2))
                );

    }

    @Test
    void givenApiCodeWhenGetByApiCodeShouldReturnRelatedMovieDTOAndReturn200()
            throws Exception {

        Movie movie = Movie
                .builder()
                .id(1L)
                .title("movie")
                .releasedAt(2000)
                .pictureUrl("http://movie.jpg")
                .apiCode("MOVIE")
                .build();

        Mockito.when(this.movieService.readByApiCode(anyString())).thenReturn(movie);

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/movies/apicodes/{apicode}",
                                        movie.getApiCode()
                                )
                )
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id", equalTo(movie.getId().intValue())),
                        jsonPath("$.apiCode", equalTo(movie.getApiCode()))
                );

    }

    @Test
    void givenInvalidApiCodeWhenGetByApiCodeShouldReturn404() throws Exception {

        Mockito.when(this.movieService.readByApiCode(anyString())).thenThrow(CustomNotFoundException.class);

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/movies/apicode/{apicode}",
                                        "notExistingAPiCode"
                                )
                )
                .andDo(print())
                .andExpect(
                        status().isNotFound()
                );

    }

    @Test
    void givenIdWhenGetByIdShouldReturnRelatedMovieDTOAndReturn200() throws Exception {

        Movie movie = Movie
                .builder()
                .id(1L)
                .title("movie")
                .releasedAt(2000)
                .pictureUrl("http://movie.jpg")
                .apiCode("MOVIE")
                .build();

        Mockito.when(this.movieService.readById(anyLong())).thenReturn(movie);

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/movies/{movieId}",
                                        movie.getId()
                                )
                )
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id", equalTo(movie.getId().intValue())),
                        jsonPath("$.apiCode", equalTo(movie.getApiCode()))
                );

    }

    @Test
    void givenInvalidIdWhenGetByIdShouldReturn404() throws Exception {

        Mockito.when(this.movieService.readById(anyLong())).thenThrow(CustomNotFoundException.class);

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/movies/{movieId}",
                                        "1"
                                )
                )
                .andDo(print())
                .andExpect(
                        status().isNotFound()
                );

    }

    @Test
    void givenIdWhenDeleteByIdShouldDeleteAndReturnRelatedMovieIdAndReturn200() throws Exception {

        Movie movie = Movie
                .builder()
                .id(1L)
                .title("movie")
                .releasedAt(2000)
                .pictureUrl("http://movie.jpg")
                .apiCode("MOVIE")
                .build();

        Mockito.when(this.movieService.deleteById(anyLong())).thenReturn(movie);

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/api/movies/{movieId}",
                                        movie.getId()
                                )
                )
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id", equalTo(movie.getId().intValue()))
                );

    }

    @Test
    void givenInvalidIdWhenDeleteByIdShouldReturn404() throws Exception {

        Mockito.when(this.movieService.deleteById(anyLong())).thenThrow(new CustomNotFoundException("Error"));

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/api/movies/{movieId}",
                                        1L
                                )
                )
                .andDo(print())
                .andExpect(
                        status().isNotFound()
                );
    }

    @Test
    void givenMovieAddWhenAddByApiCodeShouldSaveAndReturnRelatedMovieAndReturn200() throws Exception {

        MovieAddDTO movieAddDTO = new MovieAddDTO();
        movieAddDTO.setApiCode("XXXX");

        Movie movie = Movie.builder().id(1L).apiCode("test").pictureUrl("test.com").releasedAt(1993).build();

        Mockito.when(this.movieService.addByApiCode(anyString())).thenReturn(movie);

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/movies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        new ObjectMapper()
                                                .configure(
                                                        SerializationFeature.WRAP_ROOT_VALUE,
                                                        false
                                                )
                                                .writeValueAsString(movieAddDTO)
                                )
                )
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.apiCode", equalTo(movie.getApiCode()))
                );

    }

    @Test
    void givenMovieAddWithInvalidApiCodeWhenAddByApiCodeShouldThrowNotFoundExceptionAndReturn400() throws Exception {

        MovieAddDTO movieAddDTO = new MovieAddDTO();
        movieAddDTO.setApiCode("XXXX");

        Mockito.when(this.movieService.addByApiCode(anyString())).thenThrow(CustomBadRequestException.class);

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/movies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        new ObjectMapper()
                                                .configure(
                                                        SerializationFeature.WRAP_ROOT_VALUE,
                                                        false
                                                )
                                                .writeValueAsString(movieAddDTO)
                                )
                )
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        result -> assertTrue(result.getResolvedException() instanceof CustomBadRequestException)
                );
    }

    @Test
    void givenMovieAddWhenAddByApiCodeAndApiNotAvailableShouldThrowServiceNotAvailableExceptionAndReturn424() throws Exception {

        MovieAddDTO movieAddDTO = new MovieAddDTO();
        movieAddDTO.setApiCode("XXXX");

        Mockito.when(this.movieService.addByApiCode(anyString())).thenThrow(ServiceNotAvailableException.class);

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/movies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        new ObjectMapper()
                                                .configure(
                                                        SerializationFeature.WRAP_ROOT_VALUE,
                                                        false
                                                )
                                                .writeValueAsString(movieAddDTO)
                                )
                )
                .andDo(print())
                .andExpectAll(
                        status().isFailedDependency(),
                        result -> assertTrue(result.getResolvedException() instanceof ServiceNotAvailableException)
                );

    }
}