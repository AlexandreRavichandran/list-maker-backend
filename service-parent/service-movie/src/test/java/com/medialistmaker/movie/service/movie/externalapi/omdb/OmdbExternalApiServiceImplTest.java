package com.medialistmaker.movie.service.movie.externalapi.omdb;

import com.medialistmaker.movie.dto.externalapi.omdbapi.collection.MovieElementListDTO;
import com.medialistmaker.movie.dto.externalapi.omdbapi.collection.MovieElementListItemDTO;
import com.medialistmaker.movie.dto.externalapi.omdbapi.item.MovieElementDTO;
import com.medialistmaker.movie.exception.badrequestexception.CustomBadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class OmdbExternalApiServiceImplTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    OmdbExternalApiServiceImpl omdbExternalApiService;

    @Test
    void givenMovieNameWhenGetByMovieNameShouldReturnRelatedMovieList() throws CustomBadRequestException {

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
        listDTO.setTotalResults("5");
        listDTO.setMovieElementList(List.of(firstListItemDTO, secondListItemDTO));


        Mockito.when(this.restTemplate.getForEntity(anyString(), any())).thenReturn(new ResponseEntity<>(listDTO, HttpStatus.OK));

        MovieElementListDTO testGetByMovieName = this.omdbExternalApiService.getByMovieName("test");

        Mockito.verify(this.restTemplate).getForEntity(anyString(), any());
        assertNotNull(testGetByMovieName);
        assertEquals(listDTO, testGetByMovieName);
    }

    @Test
    void givenMovieNameWhenGetByMovieNameAndApiNotAvailableShouldThrowException() {

        Mockito.when(this.restTemplate.getForEntity(anyString(), any())).thenReturn(new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE));

        assertThrows(CustomBadRequestException.class, () -> this.omdbExternalApiService.getByMovieName("test"));

        Mockito.verify(this.restTemplate).getForEntity(anyString(), any());

    }

    @Test
    void givenApiCodeWhenGetByApiCodeShouldReturnRelatedMovie() throws CustomBadRequestException {

        MovieElementDTO movieElement = new MovieElementDTO();
        movieElement.setApiCode("00001");
        movieElement.setTitle("Movie 1");
        movieElement.setDirector("Alexandre");
        movieElement.setDuration("200m");
        movieElement.setSynopsis("Summary");
        movieElement.setReleasedAt("2000");

        Mockito.when(this.restTemplate.getForEntity(anyString(), any())).thenReturn(new ResponseEntity<>(movieElement, HttpStatus.OK));

        MovieElementDTO testGetByApiCode = this.omdbExternalApiService.getByApiCode("test");

        Mockito.verify(this.restTemplate).getForEntity(anyString(), any());
        assertNotNull(testGetByApiCode);
        assertEquals(movieElement, testGetByApiCode);

    }

    @Test
    void givenInvalidApiCodeWhenGetByApiCodeShouldThrowException() {

        Mockito.when(this.restTemplate.getForEntity(anyString(), any())).thenReturn(new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE));

        assertThrows(CustomBadRequestException.class, () -> this.omdbExternalApiService.getByApiCode("test"));

        Mockito.verify(this.restTemplate).getForEntity(anyString(), any());

    }
}