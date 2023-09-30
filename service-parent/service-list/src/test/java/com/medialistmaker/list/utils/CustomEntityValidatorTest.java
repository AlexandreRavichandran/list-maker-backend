package com.medialistmaker.list.utils;

import com.medialistmaker.list.domain.AppUserMusicListItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomEntityValidatorTest {

    @Autowired
    CustomEntityValidator<AppUserMusicListItem> movieEntityValidator;

    @BeforeEach
    void beforeAllTests() {
        this.movieEntityValidator = new CustomEntityValidator<>();
    }

    @Test
    void givenValidEntityWhenValidateEntityShouldReturnEmptyList() {

        AppUserMusicListItem validMusicListItem = AppUserMusicListItem
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

        AppUserMusicListItem invalidMusicListItem = AppUserMusicListItem
                .builder()
                .musicId(1L)
                .build();

        List<String> testInvalidEntity = this.movieEntityValidator.validateEntity(invalidMusicListItem);

        assertTrue(testInvalidEntity.size() > 0);

    }
}