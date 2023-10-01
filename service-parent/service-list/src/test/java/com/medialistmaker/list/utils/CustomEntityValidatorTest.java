package com.medialistmaker.list.utils;

import com.medialistmaker.list.domain.MusicListItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomEntityValidatorTest {

    @Autowired
    CustomEntityValidator<MusicListItem> movieEntityValidator;

    @BeforeEach
    void beforeAllTests() {
        this.movieEntityValidator = new CustomEntityValidator<>();
    }

    @Test
    void givenValidEntityWhenValidateEntityShouldReturnEmptyList() {

        MusicListItem validMusicListItem = MusicListItem
                .builder()
                .musicId(1L)
                .appUserId(1L)
                .sortingOrder(1)
                .addedAt(new Date())
                .build();

        List<String> testValidEntity = this.movieEntityValidator.validateEntity(validMusicListItem);

        assertEquals(0, testValidEntity.size());

    }

    @Test
    void givenInvalidEntityWhenValidateEntityShouldReturnErrorList() {

        MusicListItem invalidMusicListItem = MusicListItem
                .builder()
                .musicId(1L)
                .build();

        List<String> testInvalidEntity = this.movieEntityValidator.validateEntity(invalidMusicListItem);

        assertTrue(testInvalidEntity.size() > 0);

    }
}