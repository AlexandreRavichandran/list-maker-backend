package com.medialistmaker.music.service.externalapi.deezer.search;

import com.medialistmaker.music.dto.externalapi.deezerapi.ArtistElementDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.search.item.AlbumSearchElementDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.search.list.AlbumSearchListDTO;
import com.medialistmaker.music.exception.badrequestexception.CustomBadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class AlbumSearchApiServiceImplTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    AlbumSearchApiServiceImpl albumSearchApiService;

    @Test
    void givenAlbumNameWhenGetByAlbumNameShouldReturnRelatedSearchAlbumElementList() throws CustomBadRequestException {

        ArtistElementDTO artist = new ArtistElementDTO();
        artist.setId("1");
        artist.setName("Artist");

        AlbumSearchElementDTO firstAlbum = new AlbumSearchElementDTO();
        firstAlbum.setId("1L");
        firstAlbum.setTitle("Album 1");
        firstAlbum.setPictureUrl("test.com");
        firstAlbum.setArtist(artist);

        AlbumSearchElementDTO secondAlbum = new AlbumSearchElementDTO();
        secondAlbum.setId("1L");
        secondAlbum.setTitle("Album 1");
        secondAlbum.setPictureUrl("test.com");
        secondAlbum.setArtist(artist);

        AlbumSearchListDTO albumSearchListDTO = new AlbumSearchListDTO();
        albumSearchListDTO.setData(List.of(firstAlbum, secondAlbum));

        Mockito
                .when(this.restTemplate.getForEntity(anyString(), any()))
                .thenReturn(new ResponseEntity<>(albumSearchListDTO, HttpStatus.OK));

        AlbumSearchListDTO testGetByAlbumName = this.albumSearchApiService.getByAlbumName("test");

        Mockito.verify(this.restTemplate).getForEntity(anyString(), any());
        assertNotNull(testGetByAlbumName);
        assertEquals(2, albumSearchListDTO.getData().size());

    }

    @Test
    void givenAlbumNameWhenGetByAlbumNameAndApiNotAvailableShouldThrowException() {

        Mockito
                .when(this.restTemplate.getForEntity(anyString(), any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE));

        assertThrows(CustomBadRequestException.class, () -> this.albumSearchApiService.getByAlbumName("test"));

        Mockito.verify(this.restTemplate).getForEntity(anyString(), any());

    }
}