package com.medialistmaker.music.connector.deezer.album;

import com.medialistmaker.music.dto.externalapi.deezerapi.AlbumElementDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.ArtistElementDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.SongElementDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.TrackListDTO;
import com.medialistmaker.music.exception.badrequestexception.CustomBadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

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
        album.setApiCode("1L");
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

        assertThrows(CustomBadRequestException.class, () -> this.albumConnectorProxy.getByApiCode("test"));

        Mockito.verify(this.deezerAlbumConnector).getByApiCode(anyString());

    }

    @Test
    void givenApiCodeWhenGetTrackListByApiCodeShouldReturnRelatedSongElementList() throws Exception {

        SongElementDTO firstElement = new SongElementDTO();
        firstElement.setApiCode("1");

        SongElementDTO secondElement = new SongElementDTO();
        secondElement.setApiCode("1");

        SongElementDTO thirdElement = new SongElementDTO();
        thirdElement.setApiCode("1");

        TrackListDTO trackListDTO = new TrackListDTO();
        trackListDTO.setSongs(List.of(firstElement, secondElement, thirdElement));

        Mockito.when(this.deezerAlbumConnector.getTrackListByAlbumApiCode(anyString())).thenReturn(trackListDTO);

        TrackListDTO testGetTrackListByApiCode = this.albumConnectorProxy.getTrackListByAlbumApiCode("test");

        assertNotNull(testGetTrackListByApiCode);
        assertEquals(trackListDTO, testGetTrackListByApiCode);
        Mockito.verify(this.deezerAlbumConnector).getTrackListByAlbumApiCode(anyString());

    }

    @Test
    void givenApiCodeWhenGetTrackListByApiCodeAndServiceNotAvailableShouldThrowBadRequestException() throws Exception {

        Mockito.when(this.deezerAlbumConnector.getTrackListByAlbumApiCode(anyString())).thenThrow(CustomBadRequestException.class);

        assertThrows(CustomBadRequestException.class, () -> this.albumConnectorProxy.getTrackListByAlbumApiCode("test"));

        Mockito.verify(this.deezerAlbumConnector).getTrackListByAlbumApiCode(anyString());

    }
}