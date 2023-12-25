package com.medialistmaker.list.connector.music;

import com.medialistmaker.list.dto.music.MusicAddDTO;
import com.medialistmaker.list.dto.music.MusicDTO;
import com.medialistmaker.list.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.list.exception.servicenotavailableexception.ServiceNotAvailableException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MusicConnectorProxyTest {

    @Mock
    MusicConnector musicConnector;

    @InjectMocks
    MusicConnectorProxy musicConnectorProxy;

    @Test
    void givenApiCodeWhenAddByApiCodeShouldReturnRelatedMusic() throws Exception {

        MusicDTO musicDTO = new MusicDTO();
        musicDTO.setId(1L);

        MusicAddDTO musicAddDTO = new MusicAddDTO();
        musicAddDTO.setApiCode("test");
        musicAddDTO.setType(1);

        Mockito.when(this.musicConnector.saveByApiCode(musicAddDTO)).thenReturn(musicDTO);

        MusicDTO testGetByApiCode = this.musicConnectorProxy.saveByApiCode(musicAddDTO);

        assertNotNull(testGetByApiCode);
        assertEquals(musicDTO, testGetByApiCode);

    }

    @Test
    void givenInvalidApiCodeWhenAddByApiCodeShouldThrowBadRequestException() throws Exception {

        MusicAddDTO musicAddDTO = new MusicAddDTO();
        musicAddDTO.setApiCode("test");
        musicAddDTO.setType(1);

        Mockito.when(this.musicConnector.saveByApiCode(musicAddDTO)).thenThrow(CustomBadRequestException.class);

        assertThrows(CustomBadRequestException.class, () -> this.musicConnectorProxy.saveByApiCode(musicAddDTO));

    }

    @Test
    void givenApiCodeWhenAddByApiCodeAndServiceNotAvailableShouldThrowServiceNotAvailableException() throws Exception {

        MusicAddDTO musicAddDTO = new MusicAddDTO();
        musicAddDTO.setApiCode("test");
        musicAddDTO.setType(1);


        Mockito.when(this.musicConnector.saveByApiCode(musicAddDTO)).thenThrow(ServiceNotAvailableException.class);

        assertThrows(ServiceNotAvailableException.class, () -> this.musicConnectorProxy.saveByApiCode(musicAddDTO));

    }
}