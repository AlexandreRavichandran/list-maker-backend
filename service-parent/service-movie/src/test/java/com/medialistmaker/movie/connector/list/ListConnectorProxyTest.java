package com.medialistmaker.movie.connector.list;

import com.medialistmaker.movie.exception.servicenotavailableexception.ServiceNotAvailableException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class ListConnectorProxyTest {

    @Mock
    ListConnector listConnector;

    @InjectMocks
    ListConnectorProxy listConnectorProxy;

    @Test
    void givenMovieIdWhenIsMovieIdAlreadyInListShouldReturnBoolean() throws ServiceNotAvailableException {

        Mockito.when(this.listConnector.isMovieIdAlreadyInList(anyLong())).thenReturn(Boolean.TRUE);

        Boolean testIsMovieIdAlreadyInList = this.listConnectorProxy.isMovieIdAlreadyInList(1L);

        assertNotNull(testIsMovieIdAlreadyInList);
        assertEquals(Boolean.TRUE, testIsMovieIdAlreadyInList);
    }
}