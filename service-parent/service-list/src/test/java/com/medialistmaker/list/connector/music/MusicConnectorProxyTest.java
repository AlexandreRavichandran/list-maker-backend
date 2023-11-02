package com.medialistmaker.list.connector.music;

import com.medialistmaker.list.dto.music.MusicDTO;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class MusicConnectorProxyTest {

    @Mock
    MusicConnector musicConnector;

    @InjectMocks
    MusicConnectorProxy musicConnectorProxy;

    @Test
    void givenApiCodeWhenGetAlbumByApiCodeShouldReturnRelatedMusic() throws Exception {

        MusicDTO musicDTO = new MusicDTO();
        musicDTO.setId(1L);

        Mockito.when(this.musicConnector.getAlbumByApiCode(anyString())).thenReturn(musicDTO);

        MusicDTO testGetByApiCode = this.musicConnectorProxy.getAlbumByApiCode("test");

        assertNotNull(testGetByApiCode);
        assertEquals(musicDTO, testGetByApiCode);
    }

    @Test
    void givenInvalidApiCodeWhenGetAlbumByApiCodeShouldThrowNotFoundException() throws Exception {

        Mockito.when(this.musicConnector.getAlbumByApiCode(anyString())).thenThrow(CustomNotFoundException.class);

        assertThrows(CustomNotFoundException.class, () -> this.musicConnectorProxy.getAlbumByApiCode("test"));

    }

    @Test
    void givenApiCodeWhenGetAlbumByApiCodeAndServiceNotAvailableShouldThrowServiceNotAvailableException() throws Exception {

        Mockito.when(this.musicConnector.getAlbumByApiCode(anyString())).thenThrow(ServiceNotAvailableException.class);

        assertThrows(ServiceNotAvailableException.class, () -> this.musicConnectorProxy.getAlbumByApiCode("test"));

    }

    @Test
    void givenApiCodeWhenGetSongByApiCodeShouldReturnRelatedMusic() throws Exception {

        MusicDTO musicDTO = new MusicDTO();
        musicDTO.setId(1L);

        Mockito.when(this.musicConnector.getSongByApiCode(anyString())).thenReturn(musicDTO);

        MusicDTO testGetByApiCode = this.musicConnectorProxy.getSongByApiCode("test");

        assertNotNull(testGetByApiCode);
        assertEquals(musicDTO, testGetByApiCode);
    }

    @Test
    void givenInvalidApiCodeWhenGetSongByApiCodeShouldThrowNotFoundException() throws Exception {

        Mockito.when(this.musicConnector.getSongByApiCode(anyString())).thenThrow(CustomNotFoundException.class);

        assertThrows(CustomNotFoundException.class, () -> this.musicConnectorProxy.getSongByApiCode("test"));

    }

    @Test
    void givenApiCodeWhenGetSongByApiCodeAndServiceNotAvailableShouldThrowServiceNotAvailableException() throws Exception {

        Mockito.when(this.musicConnector.getSongByApiCode(anyString())).thenThrow(ServiceNotAvailableException.class);

        assertThrows(ServiceNotAvailableException.class, () -> this.musicConnectorProxy.getSongByApiCode("test"));

    }

    @Test
    void givenApiCodeWhenAddByApiCodeShouldReturnRelatedMusic() throws Exception {

        MusicDTO musicDTO = new MusicDTO();
        musicDTO.setId(1L);

        Mockito.when(this.musicConnector.saveByApiCode(anyInt(), anyString())).thenReturn(musicDTO);

        MusicDTO testGetByApiCode = this.musicConnectorProxy.saveByApiCode(1,"test");

        assertNotNull(testGetByApiCode);
        assertEquals(musicDTO, testGetByApiCode);

    }

    @Test
    void givenInvalidApiCodeWhenAddByApiCodeShouldThrowBadRequestException() throws Exception {

        Mockito.when(this.musicConnector.saveByApiCode(anyInt(), anyString())).thenThrow(CustomBadRequestException.class);

        assertThrows(CustomBadRequestException.class, () -> this.musicConnectorProxy.saveByApiCode(1,"test"));

    }

    @Test
    void givenApiCodeWhenAddByApiCodeAndServiceNotAvailableShouldThrowServiceNotAvailableException() throws Exception {

        Mockito.when(this.musicConnector.saveByApiCode(anyInt(), anyString())).thenThrow(ServiceNotAvailableException.class);

        assertThrows(ServiceNotAvailableException.class, () -> this.musicConnectorProxy.saveByApiCode(1,"test"));

    }
}