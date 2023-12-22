package com.medialistmaker.list.connector.movie;

import com.medialistmaker.list.dto.movie.MovieAddDTO;
import com.medialistmaker.list.dto.movie.MovieDTO;
import com.medialistmaker.list.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.list.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.list.exception.servicenotavailableexception.ServiceNotAvailableException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class MovieConnectorProxyTest {

    @Mock
    MovieConnector movieConnector;

    @InjectMocks
    MovieConnectorProxy movieConnectorProxy;

    @Test
    void givenApiCodeWhenGetByApiCodeShouldReturnRelatedMovie() throws Exception {

        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(1L);

        Mockito.when(this.movieConnector.getByApiCode(anyString())).thenReturn(movieDTO);

        MovieDTO testGetByApiCode = this.movieConnectorProxy.getByApiCode("test");

        assertNotNull(movieDTO);
        assertEquals(movieDTO, testGetByApiCode);
    }

    @Test
    void givenInvalidApiCodeWhenGetByApiCodeShouldThrowNotFoundException() throws Exception {

        Mockito.when(this.movieConnector.getByApiCode(anyString())).thenThrow(CustomNotFoundException.class);

        assertThrows(CustomNotFoundException.class, () -> this.movieConnectorProxy.getByApiCode("test"));

    }

    @Test
    void givenApiCodeWhenGetByApiCodeAndServiceNotAvailableShouldThrowServiceNotAvailableException() throws Exception {

        Mockito.when(this.movieConnector.getByApiCode(anyString())).thenThrow(ServiceNotAvailableException.class);

        assertThrows(ServiceNotAvailableException.class, () -> this.movieConnectorProxy.getByApiCode("test"));

    }

    @Test
    void givenApiCodeWhenAddByApiCodeShouldReturnRelatedMovie() throws Exception {

        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(1L);

        MovieAddDTO movieAddDTO = new MovieAddDTO();
        movieAddDTO.setApiCode("XXX");

        Mockito.when(this.movieConnector.saveByApiCode(movieAddDTO)).thenReturn(movieDTO);

        MovieDTO testGetByApiCode = this.movieConnectorProxy.saveByApiCode(movieAddDTO);

        assertNotNull(movieDTO);
        assertEquals(movieDTO, testGetByApiCode);

    }

    @Test
    void givenInvalidApiCodeWhenAddByApiCodeShouldThrowBadRequestException() throws Exception {

        MovieAddDTO movieAddDTO = new MovieAddDTO();
        movieAddDTO.setApiCode("XXX");

        Mockito.when(this.movieConnector.saveByApiCode(movieAddDTO)).thenThrow(CustomBadRequestException.class);

        assertThrows(CustomBadRequestException.class, () -> this.movieConnectorProxy.saveByApiCode(movieAddDTO));

    }

    @Test
    void givenApiCodeWhenAddByApiCodeAndServiceNotAvailableShouldThrowServiceNotAvailableException() throws Exception {

        MovieAddDTO movieAddDTO = new MovieAddDTO();
        movieAddDTO.setApiCode("XXX");

        Mockito.when(this.movieConnector.saveByApiCode(movieAddDTO)).thenThrow(ServiceNotAvailableException.class);

        assertThrows(ServiceNotAvailableException.class, () -> this.movieConnectorProxy.saveByApiCode(movieAddDTO));

    }
}