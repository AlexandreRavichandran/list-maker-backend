package com.medialistmaker.music.utils;

import com.medialistmaker.music.domain.Music;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomEntityValidatorTest {

    @Autowired
    CustomEntityValidator<Music> musicEntityValidator;

    @BeforeEach
    void beforeAllTests() {
        this.musicEntityValidator = new CustomEntityValidator<>();
    }

    @Test
    void givenValidEntityWhenValidateEntityShouldReturnEmptyList() {

        Music validMusic = Music
                .builder()
                .title("Music")
                .apiCode("MUSIC")
                .pictureUrl("http://img.jpg")
                .artistName("Artist")
                .type(1)
                .releasedAt(2000)
                .build();

        List<String> testValidEntity = this.musicEntityValidator.validateEntity(validMusic);

        assertEquals(0, testValidEntity.size());

    }

    @Test
    void givenInvalidEntityWhenValidateEntityShouldReturnErrorList() {

        Music invalidMusic = Music
                .builder()
                .title("Music")
                .build();

        List<String> testInvalidEntity = this.musicEntityValidator.validateEntity(invalidMusic);

        assertTrue(testInvalidEntity.size() > 0);

    }
}