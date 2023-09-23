package com.medialistmaker.music.repository;

import com.medialistmaker.music.domain.Music;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MusicRepositoryTest {

    @Autowired
    MusicRepository musicRepository;

    @Test
    void givenTypeNumberWhenGetByTypeShouldReturnRelatedMusicList() {

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

        this.musicRepository.saveAll(musicList);

        List<Music> testGetByType = this.musicRepository.getByType(1);

        assertEquals(2, testGetByType.size());
        assertTrue(testGetByType.containsAll(musicList));
    }

    @Test
    void givenIdListWhenGetByIdsShouldReturnRelatedMusicList() {

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

        this.musicRepository.saveAll(musicList);

        List<Music> testGetByIds = this.musicRepository.getByIds(List.of(firstMusic.getId(), secondMusic.getId()));

        assertEquals(2, testGetByIds.size());
        assertTrue(testGetByIds.containsAll(musicList));
    }

    @Test
    void givenApiCodeWhenGetByApiCodeShouldReturnRelatedMusic() {

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

        this.musicRepository.saveAll(musicList);

        Music testGetByApiCode = this.musicRepository.getByApiCode(firstMusic.getApiCode());

        assertNotNull(testGetByApiCode);
        assertEquals(firstMusic.getId(), testGetByApiCode.getId());
    }
}