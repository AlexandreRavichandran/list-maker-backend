package com.medialistmaker.music.connector.list;

import com.medialistmaker.music.exception.servicenotavailableexception.ServiceNotAvailableException;
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
    void givenMusicIdWhenIsMusicIdAlreadyInListShouldReturnBoolean() throws ServiceNotAvailableException {

        Mockito.when(this.listConnector.isMusicIdAlreadyInList(anyLong())).thenReturn(Boolean.TRUE);

        Boolean testIsMusicIdAlreadyInList = this.listConnectorProxy.isMusicIdAlreadyInList(1L);

        assertNotNull(testIsMusicIdAlreadyInList);
        assertEquals(Boolean.TRUE, testIsMusicIdAlreadyInList);
    }
}