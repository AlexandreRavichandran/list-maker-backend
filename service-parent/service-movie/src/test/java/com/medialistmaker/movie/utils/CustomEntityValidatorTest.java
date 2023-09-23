package com.medialistmaker.movie.utils;

import com.medialistmaker.movie.domain.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomEntityValidatorTest {

    @Autowired
    CustomEntityValidator<Movie> movieEntityValidator;

    @BeforeEach
    void beforeAllTests() {
        this.movieEntityValidator = new CustomEntityValidator<>();
    }

    @Test
    void givenValidEntityWhenValidateEntityShouldReturnEmptyList() {

        Movie validMovie = Movie
                .builder()
                .title("Movie")
                .apiCode("MOVIE")
                .pictureUrl("http://img.jpg")
                .releasedAt(2000)
                .build();

        List<String> testValidEntity = this.movieEntityValidator.validateEntity(validMovie);

        assertEquals(0, testValidEntity.size());

    }

    @Test
    void givenInvalidEntityWhenValidateEntityShouldReturnErrorList() {

        Movie invalidMovie = Movie
                .builder()
                .title("Movie")
                .build();

        List<String> testInvalidEntity = this.movieEntityValidator.validateEntity(invalidMovie);

        assertTrue(testInvalidEntity.size() > 0);

    }
}