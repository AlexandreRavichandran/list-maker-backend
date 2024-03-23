package com.medialistmaker.list.repository;

import com.medialistmaker.list.domain.MusicListItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MusicListItemRepositoryTest {

    @Autowired
    MusicListItemRepository musicListItemRepository;

    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

    @Test
    void givenAppUserIdWhenGetByAppUserIdShouldReturnRelatedMusicListItemOrderedBySortingOrderAsc() {

        MusicListItem firstMusicListItem = MusicListItem
                .builder()
                .musicId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        MusicListItem secondMusicListItem = MusicListItem
                .builder()
                .musicId(2L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(2)
                .build();

        MusicListItem thirdMusicListItem = MusicListItem
                .builder()
                .musicId(3L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(3)
                .build();

        List<MusicListItem> musicList = List.of(firstMusicListItem, secondMusicListItem, thirdMusicListItem);

        this.musicListItemRepository.saveAll(musicList);

        List<MusicListItem> testGetByAppUserId = this.musicListItemRepository.getByAppUserIdOrderBySortingOrderAsc(1L);

        assertEquals(3, musicList.size());
        assertEquals(firstMusicListItem, testGetByAppUserId.get(0));
        assertEquals(secondMusicListItem, testGetByAppUserId.get(1));
        assertEquals(thirdMusicListItem, testGetByAppUserId.get(2));
    }

    @Test
    void givenAppUserIdAndMusicIdWhenGetByAppUserIdAndMusicIdShouldReturnRelatedMusicListItem() {

        MusicListItem musicListItem = MusicListItem
                .builder()
                .musicId(1L)
                .appUserId(2L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        this.musicListItemRepository.save(musicListItem);

        MusicListItem testGetByAppUserIdAndMusicId =
                this.musicListItemRepository.getByAppUserIdAndMusicId(
                        musicListItem.getAppUserId(), musicListItem.getMusicId()
                );

        assertNotNull(testGetByAppUserIdAndMusicId);
        assertEquals(musicListItem, testGetByAppUserIdAndMusicId);

    }

    @Test
    void givenAppUserIdAndSortingOrderWhenGetByAppUserIdAndSortingOrderShouldReturnRelatedMusicListItem() {

        MusicListItem musicListItem = MusicListItem
                .builder()
                .musicId(1L)
                .appUserId(2L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        this.musicListItemRepository.save(musicListItem);

        MusicListItem testGetByAppUserIdAndSortingOrder = this.
                musicListItemRepository.getByAppUserIdAndSortingOrder(
                        musicListItem.getAppUserId(), musicListItem.getSortingOrder()
                );

        assertNotNull(testGetByAppUserIdAndSortingOrder);
        assertEquals(musicListItem, testGetByAppUserIdAndSortingOrder);
    }

    @Test
    void givenAppUserIdWhenFindFirstBySortingOrderShouldReturnMusicListItemWithMaxSortingOrder() {

        MusicListItem firstMusicListItem = MusicListItem
                .builder()
                .musicId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        MusicListItem secondMusicListItem = MusicListItem
                .builder()
                .musicId(2L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(2)
                .build();

        MusicListItem thirdMusicListItem = MusicListItem
                .builder()
                .musicId(3L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(3)
                .build();

        List<MusicListItem> musicList = List.of(firstMusicListItem, secondMusicListItem, thirdMusicListItem);

        this.musicListItemRepository.saveAll(musicList);

        MusicListItem testGetLargestSortingNumber = this.musicListItemRepository.getFirstByAppUserIdOrderBySortingOrderDesc(1L);

        assertEquals(thirdMusicListItem, testGetLargestSortingNumber);
        assertEquals(3, testGetLargestSortingNumber.getSortingOrder());
    }

    @Test
    void givenAppUserIdWhenGetTop3ByAppUserIdShouldReturnLastThreeLastAddedMusicListItem() throws ParseException {

        MusicListItem firstMusicListItem = MusicListItem
                .builder()
                .musicId(1L)
                .appUserId(1L)
                .addedAt(this.format.parse("01-01-2023"))
                .sortingOrder(1)
                .build();

        MusicListItem secondMusicListItem = MusicListItem
                .builder()
                .musicId(2L)
                .appUserId(1L)
                .addedAt(this.format.parse("02-01-2023"))
                .sortingOrder(2)
                .build();

        MusicListItem thirdMusicListItem = MusicListItem
                .builder()
                .musicId(3L)
                .appUserId(1L)
                .addedAt(this.format.parse("03-01-2023"))
                .sortingOrder(3)
                .build();

        MusicListItem fourthMusicListItem = MusicListItem
                .builder()
                .musicId(3L)
                .appUserId(1L)
                .addedAt(this.format.parse("04-01-2023"))
                .sortingOrder(4)
                .build();

        List<MusicListItem> musicList = List.of(
                firstMusicListItem,
                secondMusicListItem,
                thirdMusicListItem,
                fourthMusicListItem
        );

        this.musicListItemRepository.saveAll(musicList);

        List<MusicListItem> testGetLatestAddedMusicListItem =
                this.musicListItemRepository.getTop3ByAppUserIdOrderByAddedAtDesc(1L);

        assertEquals(3, testGetLatestAddedMusicListItem.size());
        assertEquals(fourthMusicListItem, testGetLatestAddedMusicListItem.get(0));
        assertEquals(thirdMusicListItem, testGetLatestAddedMusicListItem.get(1));
        assertEquals(secondMusicListItem, testGetLatestAddedMusicListItem.get(2));
    }
}