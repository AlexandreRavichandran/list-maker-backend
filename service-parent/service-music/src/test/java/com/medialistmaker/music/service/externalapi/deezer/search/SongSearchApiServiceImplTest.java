package com.medialistmaker.music.service.externalapi.deezer.search;

import com.medialistmaker.music.dto.externalapi.deezerapi.ArtistElementDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.search.item.SongSearchElementDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.search.list.SongSearchListDTO;
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
class SongSearchApiServiceImplTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    SongSearchApiServiceImpl songSearchApiService;

    @Test
    void givenSongNameWhenGetBySongNameShouldReturnRelatedSearchSongElementList() throws CustomBadRequestException {

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

        Mockito
                .when(this.restTemplate.getForEntity(anyString(), any()))
                .thenReturn(new ResponseEntity<>(songSearchListDTO, HttpStatus.OK));

        SongSearchListDTO testGetBySongName = this.songSearchApiService.getBySongName("test");

        Mockito.verify(this.restTemplate).getForEntity(anyString(), any());
        assertNotNull(testGetBySongName);
        assertEquals(2, songSearchListDTO.getData().size());

    }

    @Test
    void givenSongNameWhenGetBySongNameAndApiNotAvailableShouldThrowException() {

        Mockito
                .when(this.restTemplate.getForEntity(anyString(), any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE));

        assertThrows(CustomBadRequestException.class, () -> this.songSearchApiService.getBySongName("test"));

        Mockito.verify(this.restTemplate).getForEntity(anyString(), any());

    }
}