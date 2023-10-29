package com.medialistmaker.music.connector.deezer.search;

import com.medialistmaker.music.dto.externalapi.deezerapi.ArtistElementDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.search.item.AlbumSearchElementDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.search.item.SongSearchElementDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.search.list.AlbumSearchListDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.search.list.SongSearchListDTO;
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
class DeezerSearchConnectorProxyTest {

    @Mock
    DeezerSearchConnector deezerSearchConnector;

    @InjectMocks
    DeezerSearchConnectorProxy deezerSearchConnectorProxy;

    @Test
    void givenQueryWhenGetAlbumByQueryShouldReturnRelatedSearchList() throws Exception {

        ArtistElementDTO artist = new ArtistElementDTO();
        artist.setId("1");
        artist.setName("Artist");

        AlbumSearchElementDTO firstAlbum = new AlbumSearchElementDTO();
        firstAlbum.setId("1L");
        firstAlbum.setTitle("Album 1");
        firstAlbum.setPictureUrl("test.com");
        firstAlbum.setArtist(artist);

        AlbumSearchElementDTO secondAlbum = new AlbumSearchElementDTO();
        secondAlbum.setId("2L");
        secondAlbum.setTitle("Album 2");
        secondAlbum.setPictureUrl("test.com");
        secondAlbum.setArtist(artist);

        AlbumSearchListDTO albumSearchListDTO = new AlbumSearchListDTO();
        albumSearchListDTO.setData(List.of(firstAlbum, secondAlbum));

        Mockito.when(this.deezerSearchConnector.getAlbumByQuery(anyString())).thenReturn(albumSearchListDTO);

        AlbumSearchListDTO testGetByApiCode = this.deezerSearchConnectorProxy.getAlbumByQuery("test");

        assertNotNull(testGetByApiCode);
        assertEquals(albumSearchListDTO, testGetByApiCode);
        Mockito.verify(this.deezerSearchConnector).getAlbumByQuery(anyString());

    }

    @Test
    void givenQueryWhenGetAlbumByQueryAndApiNotAvailableShouldThrowBadRequestException() throws Exception {

        Mockito.when(this.deezerSearchConnector.getAlbumByQuery(anyString())).thenThrow(CustomBadRequestException.class);

        assertThrows(CustomBadRequestException.class, () -> this.deezerSearchConnectorProxy.getAlbumByQuery("test"));

        Mockito.verify(this.deezerSearchConnector).getAlbumByQuery(anyString());

    }

    @Test
    void givenQueryWhenGetSongByQueryShouldReturnRelatedSearchList() throws Exception {

        ArtistElementDTO artist = new ArtistElementDTO();
        artist.setId("1");
        artist.setName("Artist");

        SongSearchElementDTO firstSong = new SongSearchElementDTO();
        firstSong.setId("1L");
        firstSong.setTitle("Song 1");
        firstSong.setDuration("2m30");
        firstSong.setPreview("test.mp3");
        firstSong.setArtist(artist);

        SongSearchElementDTO secondSong = new SongSearchElementDTO();
        secondSong.setId("2L");
        secondSong.setTitle("Song 2");
        secondSong.setDuration("2m30");
        secondSong.setPreview("test.mp3");
        secondSong.setArtist(artist);

        SongSearchListDTO songSearchListDTO = new SongSearchListDTO();
        songSearchListDTO.setData(List.of(firstSong, secondSong));

        Mockito.when(this.deezerSearchConnector.getSongByQuery(anyString())).thenReturn(songSearchListDTO);

        SongSearchListDTO testGetByApiCode = this.deezerSearchConnectorProxy.getSongByQuery("test");

        assertNotNull(testGetByApiCode);
        assertEquals(songSearchListDTO, testGetByApiCode);
        Mockito.verify(this.deezerSearchConnector).getSongByQuery(anyString());

    }

    @Test
    void givenQueryWhenGetSongByQueryAndApiNotAvailableShouldThrowBadRequestException() throws Exception {

        Mockito.when(this.deezerSearchConnector.getSongByQuery(anyString())).thenThrow(CustomBadRequestException.class);

        assertThrows(CustomBadRequestException.class, () -> this.deezerSearchConnectorProxy.getSongByQuery("test"));

        Mockito.verify(this.deezerSearchConnector).getSongByQuery(anyString());

    }
}