package com.medialistmaker.music.connector.deezer.song;

import com.medialistmaker.music.dto.externalapi.deezerapi.ArtistElementDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.SongElementDTO;
import com.medialistmaker.music.exception.badrequestexception.CustomBadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class DeezerSongConnectorProxyTest {

    @Mock
    DeezerSongConnector deezerSongConnector;

    @InjectMocks
    DeezerSongConnectorProxy deezerSongConnectorProxy;

    @Test
    void givenApiCodeWhenGetByApiCodeShouldReturnRelatedSongElement() throws Exception {

        ArtistElementDTO artist = new ArtistElementDTO();
        artist.setId("1");
        artist.setName("Artist");

        SongElementDTO song = new SongElementDTO();
        song.setId("1L");
        song.setTitle("Song 1");
        song.setDuration("2m30");
        song.setPreview("test.mp3");
        song.setArtist(artist);

        Mockito.when(this.deezerSongConnector.getByApiCode(anyString())).thenReturn(song);

        SongElementDTO testGetByApiCode = this.deezerSongConnectorProxy.getByApiCode("test");

        assertNotNull(testGetByApiCode);
        assertEquals(song, testGetByApiCode);
        Mockito.verify(this.deezerSongConnector).getByApiCode(anyString());

    }

    @Test
    void givenApiCodeWhenGetByApiCodeAndServiceNotAvailableShouldThrowBadRequestException() throws Exception {

        Mockito.when(this.deezerSongConnector.getByApiCode(anyString())).thenThrow(CustomBadRequestException.class);

        assertThrows(CustomBadRequestException.class, ()->this.deezerSongConnectorProxy.getByApiCode("test"));

        Mockito.verify(this.deezerSongConnector).getByApiCode(anyString());

    }

}