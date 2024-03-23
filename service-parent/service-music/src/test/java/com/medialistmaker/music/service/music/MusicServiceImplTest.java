package com.medialistmaker.music.service.music;

import com.medialistmaker.music.connector.deezer.album.DeezerAlbumConnectorProxy;
import com.medialistmaker.music.connector.deezer.song.DeezerSongConnectorProxy;
import com.medialistmaker.music.constant.MusicTypeConstant;
import com.medialistmaker.music.domain.Music;
import com.medialistmaker.music.dto.externalapi.deezerapi.AlbumElementDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.ArtistElementDTO;
import com.medialistmaker.music.dto.externalapi.deezerapi.SongElementDTO;
import com.medialistmaker.music.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.music.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.music.exception.servicenotavailableexception.ServiceNotAvailableException;
import com.medialistmaker.music.repository.MusicRepository;
import com.medialistmaker.music.utils.CustomEntityValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class MusicServiceImplTest {

    @Mock
    MusicRepository musicRepository;

    @Mock
    CustomEntityValidator<Music> musicEntityValidator;

    @Mock
    DeezerAlbumConnectorProxy albumConnectorProxy;

    @Mock
    DeezerSongConnectorProxy songConnectorProxy;

    @Spy
    ModelMapper modelMapper;

    @InjectMocks
    MusicServiceImpl musicService;

    @Test
    void givenIdListWhenBrowseByIdsShouldReturnRelatedMusicList() {

        Music firstMusic = Music
                .builder()
                .id(1L)
                .title("First music")
                .artistName("Artist 1")
                .type(1)
                .apiCode("MUSIC1")
                .pictureUrl("http://test.jpg")
                .releasedAt(2000)
                .build();

        Music secondMusic = Music
                .builder()
                .id(2L)
                .title("Second music")
                .artistName("Artist 2")
                .type(1)
                .apiCode("MUSIC2")
                .pictureUrl("http://test.jpg")
                .releasedAt(2002)
                .build();

        List<Music> musicList = new ArrayList<>(List.of(firstMusic, secondMusic));

        Mockito.when(this.musicRepository.getByIds(anyList())).thenReturn(musicList);

        List<Music> testBrowseByIds = this.musicService.browseByIds(List.of(1L, 2L));

        assertEquals(musicList.size(), testBrowseByIds.size());
        assertTrue(testBrowseByIds.containsAll(musicList));
        Mockito.verify(this.musicRepository).getByIds(anyList());

    }

    @Test
    void givenTypeWhenBrowseByTypeShouldReturnRelatedMusicList() {

        Music firstMusic = Music
                .builder()
                .title("First music")
                .artistName("Artist 1")
                .type(1)
                .apiCode("MUSIC1")
                .pictureUrl("http://test.jpg")
                .releasedAt(2000)
                .build();

        Music secondMusic = Music
                .builder()
                .title("Second music")
                .artistName("Artist 2")
                .type(1)
                .apiCode("MUSIC2")
                .pictureUrl("http://test.jpg")
                .releasedAt(2002)
                .build();

        List<Music> musicList = List.of(firstMusic, secondMusic);

        Mockito.when(this.musicRepository.getByType(anyInt())).thenReturn(musicList);

        List<Music> testBrowseByType = this.musicService.browseByType(1);

        assertEquals(musicList.size(), testBrowseByType.size());
        assertTrue(testBrowseByType.containsAll(musicList));
        Mockito.verify(this.musicRepository).getByType(anyInt());
    }

    @Test
    void givenIdWhenReadByIdShouldReturnRelatedMusic() throws CustomNotFoundException {

        Music music = Music
                .builder()
                .title("Music")
                .artistName("Artist")
                .type(1)
                .apiCode("MUSIC")
                .pictureUrl("http://test.jpg")
                .releasedAt(2000)
                .build();

        Mockito.when(this.musicRepository.findById(anyLong())).thenReturn(Optional.of(music));

        Music testReadById = this.musicService.readById(1L);

        assertNotNull(testReadById);
        assertEquals(music, testReadById);
        Mockito.verify(this.musicRepository).findById(anyLong());

    }

    @Test
    void givenInvalidIdWhenReadByIdShouldThrowNotFoundException() {

        Mockito.when(this.musicRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CustomNotFoundException.class, () -> this.musicService.readById(1L));
        Mockito.verify(this.musicRepository).findById(anyLong());

    }

    @Test
    void givenApiCodeWhenReadByApiCodeShouldReturnRelatedMusic() throws CustomNotFoundException {

        Music music = Music
                .builder()
                .title("Music")
                .artistName("Artist")
                .type(1)
                .apiCode("MUSIC")
                .pictureUrl("http://test.jpg")
                .releasedAt(2000)
                .build();

        Mockito.when(this.musicRepository.getByApiCodeAndType(anyString(), anyInt())).thenReturn(music);

        Music testReadByApiCode = this.musicService.readByApiCodeAndType(music.getApiCode(), 1);

        assertNotNull(testReadByApiCode);
        assertEquals(music, testReadByApiCode);
        Mockito.verify(this.musicRepository).getByApiCodeAndType(anyString(), anyInt());

    }

    @Test
    void givenInvalidApiCodeWhenReadByApiCodeShouldThrowNotFoundException() {

        Mockito.when(this.musicRepository.getByApiCodeAndType(anyString(), anyInt())).thenReturn(null);

        assertThrows(CustomNotFoundException.class, () -> this.musicService.readByApiCodeAndType("test", 1));
        Mockito.verify(this.musicRepository).getByApiCodeAndType(anyString(), anyInt());
    }

    @Test
    void givenAlbumApiCodeAndTypeWhenAddByApiCodeShouldSaveAndReturnMovie() throws Exception {

        ArtistElementDTO artistElementDTO = new ArtistElementDTO();
        artistElementDTO.setApiCode("0002");
        artistElementDTO.setName("Artist");

        AlbumElementDTO albumElementDTO = new AlbumElementDTO();
        albumElementDTO.setApiCode("0001");
        albumElementDTO.setTitle("Title");
        albumElementDTO.setPictureUrl("www.google.fr");
        albumElementDTO.setArtist(artistElementDTO);

        Music music = Music
                .builder()
                .title("Title")
                .artistName("Artist")
                .type(1)
                .apiCode("0001")
                .pictureUrl("www.google.fr")
                .build();

        Mockito.when(this.musicRepository.getByApiCodeAndType(anyString(), anyInt())).thenReturn(null);
        Mockito.when(this.albumConnectorProxy.getByApiCode(anyString())).thenReturn(albumElementDTO);
        Mockito.when(this.musicEntityValidator.validateEntity(any())).thenReturn(new ArrayList<>());
        Mockito.when(this.musicRepository.save(music)).thenReturn(music);

        Music testAddByApiCode = this.musicService.addByApiCode(1, "test");

        assertNotNull(testAddByApiCode);
        assertEquals(albumElementDTO.getApiCode(), testAddByApiCode.getApiCode());
        assertEquals(MusicTypeConstant.TYPE_ALBUM, testAddByApiCode.getType());
        Mockito.verify(this.musicRepository).getByApiCodeAndType(anyString(), anyInt());
        Mockito.verify(this.albumConnectorProxy).getByApiCode(anyString());
        Mockito.verify(this.musicRepository).save(music);
    }

    @Test
    void givenSongApiCodeAndTypeWhenAddByApiCodeShouldSaveAndReturnMovie() throws Exception {

        ArtistElementDTO artistElementDTO = new ArtistElementDTO();
        artistElementDTO.setApiCode("0002");
        artistElementDTO.setName("Artist");

        SongElementDTO songElementDTO = new SongElementDTO();
        songElementDTO.setApiCode("0001");
        songElementDTO.setTitle("Title");
        songElementDTO.setPreview("www.google.fr");
        songElementDTO.setArtist(artistElementDTO);

        Music music = Music
                .builder()
                .title("Title")
                .artistName("Artist")
                .type(2)
                .apiCode("0001")
                .build();

        Mockito.when(this.musicRepository.getByApiCodeAndType(anyString(), anyInt())).thenReturn(null);
        Mockito.when(this.songConnectorProxy.getByApiCode(anyString())).thenReturn(songElementDTO);
        Mockito.when(this.musicEntityValidator.validateEntity(any())).thenReturn(new ArrayList<>());
        Mockito.when(this.musicRepository.save(music)).thenReturn(music);

        Music testAddByApiCode = this.musicService.addByApiCode(2, "test");

        assertNotNull(testAddByApiCode);
        assertEquals(songElementDTO.getApiCode(), testAddByApiCode.getApiCode());
        assertEquals(MusicTypeConstant.TYPE_SONG, testAddByApiCode.getType());
        Mockito.verify(this.musicRepository).getByApiCodeAndType(anyString(), anyInt());
        Mockito.verify(this.songConnectorProxy).getByApiCode(anyString());
        Mockito.verify(this.musicRepository).save(music);
    }

    @Test
    void givenExistingApiCodeAndTypeWhenAddByApiCodeShouldReturnMovie() throws Exception {

        Music music = Music
                .builder()
                .title("Title")
                .artistName("Artist")
                .type(2)
                .apiCode("0001")
                .build();

        Mockito.when(this.musicRepository.getByApiCodeAndType(anyString(), anyInt())).thenReturn(music);

        Music testAddByApiCode = this.musicService.addByApiCode(2, "test");

        assertNotNull(testAddByApiCode);
        assertEquals(music.getApiCode(), testAddByApiCode.getApiCode());
        Mockito.verify(this.musicRepository).getByApiCodeAndType(anyString(), anyInt());
    }

    @Test
    void givenInvalidApiCodeAndTypeWhenAddByApiCodeShouldThrowBadRequestException() throws Exception {

        Mockito.when(this.musicRepository.getByApiCodeAndType(anyString(), anyInt())).thenReturn(null);
        Mockito.when(this.songConnectorProxy.getByApiCode(anyString())).thenReturn(null);

        assertThrows(CustomBadRequestException.class, () -> this.musicService.addByApiCode(2, "test"));
        Mockito.verify(this.musicRepository).getByApiCodeAndType(anyString(), anyInt());
        Mockito.verify(this.songConnectorProxy).getByApiCode(anyString());
    }

    @Test
    void givenApiCodeAndInvalidTypeWhenAddByApiCodeShouldThrowBadRequestException() {

        Mockito.when(this.musicRepository.getByApiCodeAndType(anyString(), anyInt())).thenReturn(null);

        assertThrows(CustomBadRequestException.class, () -> this.musicService.addByApiCode(5, "test"));
        Mockito.verify(this.musicRepository).getByApiCodeAndType(anyString(), anyInt());

    }

    @Test
    void givenApiCodeAndTypeWhenAddByApiCodeAndServiceNotAvailableShouldThrowServiceNotAvailableException() throws Exception  {

        Mockito.when(this.musicRepository.getByApiCodeAndType(anyString(), anyInt())).thenReturn(null);
        Mockito.when(this.songConnectorProxy.getByApiCode(anyString())).thenThrow(ServiceNotAvailableException.class);

        assertThrows(ServiceNotAvailableException.class, () -> this.musicService.addByApiCode(2, "test"));
        Mockito.verify(this.musicRepository).getByApiCodeAndType(anyString(), anyInt());
        Mockito.verify(this.songConnectorProxy).getByApiCode(anyString());

    }

    @Test
    void givenIdWhenDeleteByIdShouldDeleteAndReturnRelatedMusic() throws CustomNotFoundException {

        Music music = Music
                .builder()
                .title("Music")
                .artistName("Artist")
                .type(1)
                .apiCode("MUSIC")
                .pictureUrl("http://test.jpg")
                .releasedAt(2000)
                .build();

        Mockito.when(this.musicRepository.findById(anyLong())).thenReturn(Optional.of(music));

        Music testDeleteById = this.musicService.deleteById(1L);

        Mockito.verify(this.musicRepository).findById(anyLong());
        Mockito.verify(this.musicRepository).delete(music);
        assertNotNull(testDeleteById);
        assertEquals(music, testDeleteById);

    }

    @Test
    void givenInvalidIdWhenDeleteByIdShouldThrowNotFoundException() {

        Mockito.when(this.musicRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CustomNotFoundException.class, () -> this.musicService.deleteById(1L));

        Mockito.verify(this.musicRepository).findById(anyLong());

    }
}