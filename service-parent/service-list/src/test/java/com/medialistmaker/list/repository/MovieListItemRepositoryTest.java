package com.medialistmaker.list.repository;

import com.medialistmaker.list.domain.MovieListItem;
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
class MovieListItemRepositoryTest {

    @Autowired
    MovieListItemRepository movieListItemRepository;

    @Test
    void givenAppUserIdWhenGetByAppUserIdShouldReturnRelatedMovieListItemOrderedBySortingOrderAsc() {

        MovieListItem firstMovieListItem = MovieListItem
                .builder()
                .movieId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        MovieListItem secondMovieListItem = MovieListItem
                .builder()
                .movieId(2L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(2)
                .build();

        MovieListItem thirdMovieListItem = MovieListItem
                .builder()
                .movieId(3L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(3)
                .build();

        List<MovieListItem> movieList = List.of(firstMovieListItem, secondMovieListItem, thirdMovieListItem);

        this.movieListItemRepository.saveAll(movieList);

        List<MovieListItem> testGetByAppUserId = this.movieListItemRepository.getByAppUserIdOrderBySortingOrderAsc(1L);

        assertEquals(3, movieList.size());
        assertEquals(firstMovieListItem, testGetByAppUserId.get(0));
        assertEquals(secondMovieListItem, testGetByAppUserId.get(1));
        assertEquals(thirdMovieListItem, testGetByAppUserId.get(2));

    }

    @Test
    void givenAppUserIdAndMovieIdWhenGetByAppUserIdAndMusicIdShouldReturnRelatedMusicListItem() {


        MovieListItem movieListItem = MovieListItem
                .builder()
                .movieId(1L)
                .appUserId(2L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        this.movieListItemRepository.save(movieListItem);

        MovieListItem testGetByAppUserIdAndMovieId = this.
                movieListItemRepository.getByAppUserIdAndMovieId(
                        movieListItem.getAppUserId(), movieListItem.getMovieId()
                );

        assertNotNull(testGetByAppUserIdAndMovieId);
        assertEquals(movieListItem, testGetByAppUserIdAndMovieId);
    }

    @Test
    void givenAppUserIdAndSortingOrderWhenGetByAppUserIdAndSortingOrderShouldReturnRelatedMovieListItem() {

        MovieListItem movieListItem = MovieListItem
                .builder()
                .movieId(1L)
                .appUserId(2L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        this.movieListItemRepository.save(movieListItem);

        MovieListItem testGetByAppUserIdAndSortingOrder = this.
                movieListItemRepository.getByAppUserIdAndSortingOrder(
                        movieListItem.getAppUserId(), movieListItem.getSortingOrder()
                );

        assertNotNull(testGetByAppUserIdAndSortingOrder);
        assertEquals(movieListItem, testGetByAppUserIdAndSortingOrder);
    }
}