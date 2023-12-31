package com.medialistmaker.movie.connector.omdb;

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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class OmdbConnectorProxyTest {

    @Mock
    OmdbConnector omdbConnector;

    @InjectMocks
    OmdbConnectorProxy omdbConnectorProxy;

    @Test
    void givenApiCodeWhenGetByApiCodeShouldReturnRelatedMovieElement() throws Exception {

        MovieElementDTO movieElementDTO = new MovieElementDTO();
        movieElementDTO.setApiCode("XXXX");
        movieElementDTO.setTitle("");
        movieElementDTO.setDirector("");
        movieElementDTO.setDuration("anyString()");
        movieElementDTO.setPictureUrl("anyString()");

        Mockito.when(this.omdbConnector.getMovieByApiCode(anyString(), any(), anyString())).thenReturn(movieElementDTO);

        MovieElementDTO testGetByApiCode = this.omdbConnectorProxy.getByApiCode("test");

        assertEquals(movieElementDTO, testGetByApiCode);
    }

    @Test
    void givenQueryWhenGetByQueryShouldReturnRelatedMovieElementList() throws Exception {

        MovieElementListItemDTO firstItem = new MovieElementListItemDTO();
        firstItem.setTitle("First movie");
        firstItem.setPictureUrl("First url");
        firstItem.setReleasedAt("01/01/1990");
        firstItem.setApiCode("XXXX");

        MovieElementListItemDTO secondItem = new MovieElementListItemDTO();
        secondItem.setTitle("Second movie");
        secondItem.setPictureUrl("Second url");
        secondItem.setReleasedAt("01/01/2002");
        secondItem.setApiCode("XXXX");

        MovieElementListDTO movieElementListDTO = new MovieElementListDTO();
        movieElementListDTO.setSearchResults(List.of(firstItem, secondItem));
        movieElementListDTO.setTotalResults(2);

        Mockito.when(this.omdbConnector.getMoviesByQuery(anyString(), any(), anyString(), any())).thenReturn(movieElementListDTO);

        MovieElementListDTO testGetByQuery = this.omdbConnectorProxy.getByQuery("test", null);

        assertEquals(movieElementListDTO, testGetByQuery);
    }

    @Test
    void givenApiCodeWhenGetByApiCodeAndApiNotAvailableShouldThrowBadRequestException() throws Exception {

        Mockito.when(this.omdbConnector.getMovieByApiCode(anyString(), any(), anyString())).thenThrow(CustomBadRequestException.class);

        assertThrows(CustomBadRequestException.class, () ->  this.omdbConnectorProxy.getByApiCode("test"));


    }

    @Test
    void givenQueryWhenGetByQueryAndApiNotAvailableShouldThrowBadRequestException() throws Exception {

        Mockito.when(this.omdbConnector.getMoviesByQuery(anyString(), any(), anyString(), any())).thenThrow(CustomBadRequestException.class);

        assertThrows(CustomBadRequestException.class, () -> this.omdbConnectorProxy.getByQuery("test", null));
    } 
}