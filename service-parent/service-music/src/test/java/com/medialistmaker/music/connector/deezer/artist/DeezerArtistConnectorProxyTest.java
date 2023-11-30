package com.medialistmaker.music.connector.deezer.artist;

import com.medialistmaker.music.dto.externalapi.deezerapi.AlbumElementDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.AlbumListDTO;
import com.medialistmaker.music.exception.badrequestexception.CustomBadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class DeezerArtistConnectorProxyTest {

    @Mock
    DeezerArtistConnector deezerArtistConnector;

    @InjectMocks
    DeezerArtistConnectorProxy deezerArtistConnectorProxy;

    @Test
    void givenArtistIdWhenGetAlbumListByArtistIdShouldReturnRelatedAlbumList() throws Exception {

        AlbumElementDTO firstAlbum = new AlbumElementDTO();
        firstAlbum.setId("1");

        AlbumElementDTO secondAlbum = new AlbumElementDTO();
        secondAlbum.setId("2");

        AlbumElementDTO thirdAlbum = new AlbumElementDTO();
        thirdAlbum.setId("3");

        AlbumListDTO listDTO = new AlbumListDTO();
        listDTO.setAlbumList(List.of(firstAlbum, secondAlbum, thirdAlbum));

        Mockito.when(this.deezerArtistConnector.getAlbumListByArtistId(anyLong())).thenReturn(listDTO);

        AlbumListDTO testGetAlbumListByArtistId = this.deezerArtistConnectorProxy.getAlbumListByArtistId(1L);

        Mockito.verify(this.deezerArtistConnector).getAlbumListByArtistId(anyLong());
        assertEquals(listDTO, testGetAlbumListByArtistId);
    }

    @Test
    void givenArtistIdWhenGetAlbumListByArtistIdAndApiNotAvailableShouldThrowBadRequestException() throws Exception {

        Mockito
                .when(this.deezerArtistConnector.getAlbumListByArtistId(anyLong()))
                .thenThrow(CustomBadRequestException.class);

        assertThrows(CustomBadRequestException.class, () -> this.deezerArtistConnectorProxy.getAlbumListByArtistId(1L));

        Mockito.verify(this.deezerArtistConnector).getAlbumListByArtistId(anyLong());

    }
}