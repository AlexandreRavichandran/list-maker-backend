package com.medialistmaker.music.service.music;

import com.medialistmaker.music.domain.Music;
import com.medialistmaker.music.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.music.exception.entityduplicationexception.CustomEntityDuplicationException;
import com.medialistmaker.music.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.music.repository.MusicRepository;
import com.medialistmaker.music.utils.CustomEntityValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class MusicServiceImplTest {

    @Mock
    MusicRepository musicRepository;

    @Mock
    CustomEntityValidator<Music> musicEntityValidator;

    @InjectMocks
    MusicServiceImpl musicService;

    @Test
    void givenIdListWhenBrowseByIdsShouldReturnRelatedMusicList() {

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

        Mockito.when(this.musicRepository.getReferenceById(anyLong())).thenReturn(music);

        Music testReadById = this.musicService.readById(1L);

        assertNotNull(testReadById);
        assertEquals(music, testReadById);
        Mockito.verify(this.musicRepository).getReferenceById(anyLong());

    }

    @Test
    void givenInvalidIdWhenReadByIdShouldThrowNotFoundException() {

        Mockito.when(this.musicRepository.getReferenceById(anyLong())).thenReturn(null);

        assertThrows(CustomNotFoundException.class, () -> this.musicService.readById(1L));
        Mockito.verify(this.musicRepository).getReferenceById(anyLong());

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

        Mockito.when(this.musicRepository.getByApiCode(anyString())).thenReturn(music);

        Music testReadByApiCode = this.musicService.readByApiCode(music.getApiCode());

        assertNotNull(testReadByApiCode);
        assertEquals(music, testReadByApiCode);
        Mockito.verify(this.musicRepository).getByApiCode(anyString());

    }

    @Test
    void givenInvalidApiCodeWhenReadByApiCodeShouldThrowNotFoundException() {

        Mockito.when(this.musicRepository.getByApiCode(anyString())).thenReturn(null);

        assertThrows(CustomNotFoundException.class, () -> this.musicService.readByApiCode("test"));
        Mockito.verify(this.musicRepository).getByApiCode(anyString());
    }

    @Test
    void givenMusicWhenAddMusicShouldSaveAndReturnNewMusic()
            throws CustomBadRequestException, CustomEntityDuplicationException {

        Music music = Music
                .builder()
                .title("Music")
                .artistName("Artist")
                .type(1)
                .apiCode("MUSIC")
                .pictureUrl("http://test.jpg")
                .releasedAt(2000)
                .build();

        Mockito.when(this.musicRepository.getByApiCode(anyString())).thenReturn(null);
        Mockito.when(this.musicEntityValidator.validateEntity(music)).thenReturn(emptyList());
        Mockito.when(this.musicRepository.save(music)).thenReturn(music);

        Music testAdd = this.musicService.add(music);

        Mockito.verify(this.musicRepository).getByApiCode(anyString());
        Mockito.verify(this.musicRepository).save(music);
        Mockito.verify(this.musicEntityValidator).validateEntity(music);

        assertEquals(testAdd, music);

    }

    @Test
    void givenInvalidMusicWhenAddMusicShouldThrowBadRequestException() {

        Music music = Music
                .builder()
                .title("Music")
                .artistName("Artist")
                .type(1)
                .apiCode("MUSIC")
                .pictureUrl("http://test.jpg")
                .releasedAt(2000)
                .build();

        List<String> errorList = List.of("Error 1", "Error 2");
        Mockito.when(this.musicEntityValidator.validateEntity(music)).thenReturn(errorList);

        assertThrows(CustomBadRequestException.class, () -> this.musicService.add(music));

        Mockito.verify(this.musicEntityValidator).validateEntity(music);

    }

    @Test
    void givenExistingMusicWithExistingApiCodeWhenAddMusicShouldThrowEntityDuplicationException() {

        Music music = Music
                .builder()
                .title("Music")
                .artistName("Artist")
                .type(1)
                .apiCode("MUSIC")
                .pictureUrl("http://test.jpg")
                .releasedAt(2000)
                .build();

        Mockito.when(this.musicEntityValidator.validateEntity(music)).thenReturn(emptyList());
        Mockito.when(this.musicRepository.getByApiCode(anyString())).thenReturn(music);

        assertThrows(CustomEntityDuplicationException.class, () -> this.musicService.add(music));

        Mockito.verify(this.musicEntityValidator).validateEntity(music);
        Mockito.verify(this.musicRepository).getByApiCode(anyString());

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

        Mockito.when(this.musicRepository.getReferenceById(anyLong())).thenReturn(music);

        Music testDeleteById = this.musicService.deleteById(1L);

        Mockito.verify(this.musicRepository).getReferenceById(anyLong());
        Mockito.verify(this.musicRepository).delete(music);
        assertNotNull(testDeleteById);
        assertEquals(music, testDeleteById);

    }

    @Test
    void givenInvalidIdWhenDeleteByIdShouldThrowNotFoundException() {

        Mockito.when(this.musicRepository.getReferenceById(anyLong())).thenReturn(null);

        assertThrows(CustomNotFoundException.class, () -> this.musicService.deleteById(1L));

        Mockito.verify(this.musicRepository).getReferenceById(anyLong());

    }
}