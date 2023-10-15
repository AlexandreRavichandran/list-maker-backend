package com.medialistmaker.music.service.externalapi.deezer;

import com.medialistmaker.music.dto.externalapi.deezerapi.ArtistElementDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.SongElementDTO;
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
class SongApiServiceImplTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    SongApiServiceImpl songApiService;

    @Test
    void givenApiCodeWhenGetByApiCodeShouldReturnRelatedSongElement() throws CustomBadRequestException {

        ArtistElementDTO artist = new ArtistElementDTO();
        artist.setId("1");
        artist.setName("Artist");

        SongElementDTO song = new SongElementDTO();
        song.setId("1L");
        song.setTitle("Song 1");
        song.setDuration("2m30");
        song.setPreview("test.mp3");
        song.setArtist(artist);

        Mockito
                .when(this.restTemplate.getForEntity(anyString(), any()))
                .thenReturn(new ResponseEntity<>(song, HttpStatus.OK));

        SongElementDTO testGetByApiCode = this.songApiService.getByApiCode("test");

        Mockito.verify(this.restTemplate).getForEntity(anyString(), any());

        assertNotNull(testGetByApiCode);
        assertEquals(song.getId(), testGetByApiCode.getId());

    }

    @Test
    void givenApiCodeWhenGetByApiCodeAndApiNotAvailableWShouldThrowBadRequestException() {

        Mockito
                .when(this.restTemplate.getForEntity(anyString(), any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        assertThrows(CustomBadRequestException.class, () -> this.songApiService.getByApiCode("test"));

        Mockito.verify(this.restTemplate).getForEntity(anyString(), any());
    }
}