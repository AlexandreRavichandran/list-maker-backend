package com.medialistmaker.movie.controller;

import com.medialistmaker.movie.utils.FileUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.InputStream;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PictureController.class)
class PictureControllerTest {

    @MockBean
    FileUtils fileUtils;

    @Autowired
    MockMvc mockMvc;

    @Test
    void whenGetRandomIllustrationShouldReturnRandomInputStreamResourceAndReturn200() throws Exception {

        InputStream inputStream = this.getClass().getResourceAsStream("/movie_illustrations/joker.jpg");

        Mockito.when(this.fileUtils.getRandomFileInFolder(anyString())).thenReturn(inputStream);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/movies/pictures/illustrations/random")
                )
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.IMAGE_JPEG_VALUE)
                );
    }

    @Test
    void whenGetRandomIllustrationAndNoPictureAvailableShouldReturn404() throws Exception {


        Mockito.when(this.fileUtils.getRandomFileInFolder(anyString())).thenReturn(null);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/movies/pictures/illustrations/random")
                )
                .andDo(print())
                .andExpect(
                        status().isNotFound()
                );

    }

    @Test
    void whenGetRandomPosterShouldReturnRandomInputStreamResourceAndReturn200() throws Exception {

        InputStream inputStream = this.getClass().getResourceAsStream("/movie_posters/alien.jpg");

        Mockito.when(this.fileUtils.getRandomFileInFolder(anyString())).thenReturn(inputStream);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/movies/pictures/posters/random")
                )
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.IMAGE_JPEG_VALUE)
                );
    }

    @Test
    void whenGetRandomPosterAndNoPictureAvailableShouldReturn404() throws Exception {


        Mockito.when(this.fileUtils.getRandomFileInFolder(anyString())).thenReturn(null);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/movies/pictures/posters/random")
                )
                .andDo(print())
                .andExpect(
                        status().isNotFound()
                );

    }

}