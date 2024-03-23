package com.medialistmaker.movie.repository;

import com.medialistmaker.movie.domain.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MovieRepositoryTest {

    @Autowired
    MovieRepository movieRepository;

    @Test
    void givenApiCodeWhenGetByApiCodeShouldReturnRelatedMovie() {

        Movie firstMovie = Movie
                .builder()
                .apiCode("Api code 1")
                .title("Movie 1")
                .releasedAt(2020)
                .pictureUrl("http://test.com")
                .build();

        Movie secondMovie = Movie
                .builder()
                .apiCode("Api code 2")
                .title("Movie 2")
                .releasedAt(2020)
                .pictureUrl("http://test.com")
                .build();

        this.movieRepository.saveAll(List.of(firstMovie, secondMovie));

        Movie testGetByApiCode = this.movieRepository.getByApiCode("Api code 1");

        assertNotNull(testGetByApiCode);
        assertEquals(testGetByApiCode, firstMovie);
    }

    @Test
    void givenIdListWhenGetByIdListShouldReturnRelatedMovieList() {

        Movie firstMovie = Movie
                .builder()
                .apiCode("Api code 1")
                .title("Movie 1")
                .releasedAt(2020)
                .pictureUrl("http://test.com")
                .build();

        Movie secondMovie = Movie
                .builder()
                .apiCode("Api code 2")
                .title("Movie 2")
                .releasedAt(2020)
                .pictureUrl("http://test.com")
                .build();

        this.movieRepository.saveAll(List.of(firstMovie, secondMovie));

        List<Movie> testGetByIdList = this.movieRepository.getByIds(List.of(firstMovie.getId(), secondMovie.getId()));

        assertEquals(2, testGetByIdList.size());
        assertTrue(testGetByIdList.containsAll(List.of(firstMovie, secondMovie)));
    }
}