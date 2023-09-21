package com.medialistmaker.movie.repository;

import com.medialistmaker.movie.domain.Movie;
import com.netflix.discovery.converters.Auto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MovieRepositoryTest {

    @Autowired
    MovieRepository movieRepository;

    @Test
    void whenGetByApiCodeGivenApiCodeShouldReturnRelatedMovie() {

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
}