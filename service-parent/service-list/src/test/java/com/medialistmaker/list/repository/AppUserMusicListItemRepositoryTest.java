package com.medialistmaker.list.repository;

import com.medialistmaker.list.domain.AppUserMusicListItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AppUserMusicListItemRepositoryTest {

    @Autowired
    AppUserMusicListItemRepository appUserMusicListItemRepository;

    @Test
    void givenAppUserIdWhenGetByAppUserIdShouldReturnRelatedMusicListItemOrderedBySortingOrderAsc() {

        AppUserMusicListItem firstMusicListItem = AppUserMusicListItem
                .builder()
                .musicId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        AppUserMusicListItem secondMusicListItem = AppUserMusicListItem
                .builder()
                .musicId(2L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(2)
                .build();

        AppUserMusicListItem thirdMusicListItem = AppUserMusicListItem
                .builder()
                .musicId(3L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(3)
                .build();

        List<AppUserMusicListItem> musicList = List.of(firstMusicListItem, secondMusicListItem, thirdMusicListItem);

        this.appUserMusicListItemRepository.saveAll(musicList);

        List<AppUserMusicListItem> testGetByAppUserId = this.appUserMusicListItemRepository.getByAppUserIdOrderBySortingOrderAsc(1L);

        assertEquals(3, musicList.size());
        assertEquals(firstMusicListItem, testGetByAppUserId.get(0));
        assertEquals(secondMusicListItem, testGetByAppUserId.get(1));
        assertEquals(thirdMusicListItem, testGetByAppUserId.get(2));
    }

    @Test
    void givenAppUserIdAndMusicIdWhenGetByAppUserIdAndMusicIdShouldReturnRelatedMusicListItem() {

        AppUserMusicListItem musicListItem = AppUserMusicListItem
                .builder()
                .musicId(1L)
                .appUserId(2L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        this.appUserMusicListItemRepository.save(musicListItem);

        AppUserMusicListItem testGetByAppUserIdAndMusicId =
                this.appUserMusicListItemRepository.getByAppUserIdAndMusicId(
                        musicListItem.getAppUserId(), musicListItem.getMusicId()
                );

        assertNotNull(testGetByAppUserIdAndMusicId);
        assertEquals(musicListItem, testGetByAppUserIdAndMusicId);

    }

    @Test
    void givenAppUserIdAndSortingOrderWhenGetByAppUserIdAndSortingOrderShouldReturnRelatedMusicListItem() {

        AppUserMusicListItem musicListItem = AppUserMusicListItem
                .builder()
                .musicId(1L)
                .appUserId(2L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        this.appUserMusicListItemRepository.save(musicListItem);

        AppUserMusicListItem testGetByAppUserIdAndSortingOrder = this.
                appUserMusicListItemRepository.getByAppUserIdAndSortingOrder(
                        musicListItem.getAppUserId(), musicListItem.getSortingOrder()
                );

        assertNotNull(testGetByAppUserIdAndSortingOrder);
        assertEquals(musicListItem, testGetByAppUserIdAndSortingOrder);
    }
}