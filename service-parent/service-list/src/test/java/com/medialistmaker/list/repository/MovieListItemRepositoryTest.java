package com.medialistmaker.list.repository;

import com.medialistmaker.list.domain.MovieListItem;
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
class MovieListItemRepositoryTest {

    @Autowired
    MovieListItemRepository movieListItemRepository;

    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

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

    @Test
    void givenAppUserIdWhenFindFirstBySortingOrderShouldReturnMovieListItemWithMaxSortingOrder() {

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

        MovieListItem testGetLargestSortingNumber = this.
                movieListItemRepository.getFirstByAppUserIdOrderBySortingOrderDesc(1L);

        assertEquals(thirdMovieListItem, testGetLargestSortingNumber);
        assertEquals(3, testGetLargestSortingNumber.getSortingOrder());

    }

    @Test
    void givenAppUserIdWhenGetTop3ByAppUserIdShouldReturnLastThreeLastAddedMusicListItem() throws ParseException {

        MovieListItem firstMovieListItem = MovieListItem
                .builder()
                .movieId(1L)
                .appUserId(1L)
                .addedAt(this.format.parse("01-01-2023"))
                .sortingOrder(1)
                .build();

        MovieListItem secondMovieListItem = MovieListItem
                .builder()
                .movieId(2L)
                .appUserId(1L)
                .addedAt(this.format.parse("02-01-2023"))
                .sortingOrder(2)
                .build();

        MovieListItem thirdMovieListItem = MovieListItem
                .builder()
                .movieId(3L)
                .appUserId(1L)
                .addedAt(this.format.parse("03-01-2023"))
                .sortingOrder(3)
                .build();

        MovieListItem fourthMovieListItem = MovieListItem
                .builder()
                .movieId(3L)
                .appUserId(1L)
                .addedAt(this.format.parse("04-01-2023"))
                .sortingOrder(3)
                .build();

        List<MovieListItem> musicList = List.of(
                firstMovieListItem,
                secondMovieListItem,
                thirdMovieListItem,
                fourthMovieListItem
        );

        this.movieListItemRepository.saveAll(musicList);

        List<MovieListItem> testGetLatestAddedMovieListItem =
                this.movieListItemRepository.getTop3ByAppUserIdOrderByAddedAtDesc(1L);

        assertEquals(3, testGetLatestAddedMovieListItem.size());
        assertEquals(fourthMovieListItem, testGetLatestAddedMovieListItem.get(0));
        assertEquals(thirdMovieListItem, testGetLatestAddedMovieListItem.get(1));
        assertEquals(secondMovieListItem, testGetLatestAddedMovieListItem.get(2));
    }
}