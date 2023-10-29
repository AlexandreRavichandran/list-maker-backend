package com.medialistmaker.music.connector.deezer.album;

import com.medialistmaker.music.dto.externalapi.deezerapi.AlbumElementDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.ArtistElementDTO;
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
class DeezerAlbumConnectorProxyTest {

    @Mock
    DeezerAlbumConnector deezerAlbumConnector;

    @InjectMocks
    DeezerAlbumConnectorProxy albumConnectorProxy;

    @Test
    void givenApiCodeWhenGetByApiCodeShouldReturnRelatedAlbumElement() throws Exception {

        ArtistElementDTO artist = new ArtistElementDTO();
        artist.setId("1");
        artist.setName("Artist");

        AlbumElementDTO album = new AlbumElementDTO();
        album.setId("1L");
        album.setTitle("Album 1");
        album.setPictureUrl("test.com");
        album.setArtist(artist);

        Mockito.when(this.deezerAlbumConnector.getByApiCode(anyString())).thenReturn(album);

        AlbumElementDTO testGetByApiCode = this.albumConnectorProxy.getByApiCode("test");

        assertNotNull(testGetByApiCode);
        assertEquals(album, testGetByApiCode);
        Mockito.verify(this.deezerAlbumConnector).getByApiCode(anyString());

    }

    @Test
    void givenApiCodeWhenGetByApiCodeAndServiceNotAvailableShouldThrowBadRequestException() throws Exception {

        Mockito.when(this.deezerAlbumConnector.getByApiCode(anyString())).thenThrow(CustomBadRequestException.class);

        assertThrows(CustomBadRequestException.class, ()->this.albumConnectorProxy.getByApiCode("test"));

        Mockito.verify(this.deezerAlbumConnector).getByApiCode(anyString());

    }
}