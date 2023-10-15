package com.medialistmaker.music.service.externalapi.deezer;

import com.medialistmaker.music.dto.externalapi.deezerapi.AlbumElementDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.ArtistElementDTO;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class AlbumApiServiceImplTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    AlbumApiServiceImpl albumApiService;

    @Test
    void givenApiCodeWhenGetByApiCodeShouldReturnRelatedAlbumElement() throws CustomBadRequestException {

        ArtistElementDTO artist = new ArtistElementDTO();
        artist.setId("1");
        artist.setName("Artist");

        AlbumElementDTO album = new AlbumElementDTO();
        album.setId("1L");
        album.setTitle("Album 1");
        album.setPictureUrl("test.com");
        album.setArtist(artist);

        Mockito
                .when(this.restTemplate.getForEntity(anyString(), any()))
                .thenReturn(new ResponseEntity<>(album, HttpStatus.OK));

        AlbumElementDTO testGetByApiCode = this.albumApiService.getByApiCode("test");

        Mockito.verify(this.restTemplate).getForEntity(anyString(), any());

        assertNotNull(testGetByApiCode);
        assertEquals(album.getId(), testGetByApiCode.getId());

    }

    @Test
    void givenApiCodeWhenGetByApiCodeAndApiNotAvailableWShouldThrowBadRequestException() {

        Mockito
                .when(this.restTemplate.getForEntity(anyString(), any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        assertThrows(CustomBadRequestException.class, () -> this.albumApiService.getByApiCode("test"));

        Mockito.verify(this.restTemplate).getForEntity(anyString(), any());

    }
}