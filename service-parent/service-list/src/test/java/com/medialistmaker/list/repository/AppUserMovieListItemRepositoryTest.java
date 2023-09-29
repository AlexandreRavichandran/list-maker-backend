package com.medialistmaker.list.repository;

import com.medialistmaker.list.domain.AppUserMovieListItem;
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

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AppUserMovieListItemRepositoryTest {

    @Autowired
    AppUserMovieListRepository appUserMovieListRepository;

    @Test
    void givenAppUserIdWhenGetByAppUserIdShouldReturnRelatedMovieListItemOrderedBySortingOrderAsc() {

        AppUserMovieListItem firstMovieListItem = AppUserMovieListItem
                .builder()
                .movieId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        AppUserMovieListItem secondMovieListItem = AppUserMovieListItem
                .builder()
                .movieId(2L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(2)
                .build();

        AppUserMovieListItem thirdMovieListItem = AppUserMovieListItem
                .builder()
                .movieId(3L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(3)
                .build();

        List<AppUserMovieListItem> movieList = List.of(firstMovieListItem, secondMovieListItem, thirdMovieListItem);

        this.appUserMovieListRepository.saveAll(movieList);

        List<AppUserMovieListItem> testGetByAppUserId = this.appUserMovieListRepository.getByAppUserIdOrderBySortingOrderAsc(1L);

        assertEquals(3, movieList.size());
        assertEquals(firstMovieListItem, testGetByAppUserId.get(0));
        assertEquals(secondMovieListItem, testGetByAppUserId.get(1));
        assertEquals(thirdMovieListItem, testGetByAppUserId.get(2));

    }
}