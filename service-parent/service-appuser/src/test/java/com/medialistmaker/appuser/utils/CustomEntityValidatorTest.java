package com.medialistmaker.appuser.utils;

import com.medialistmaker.appuser.domain.AppUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomEntityValidatorTest {

    @Autowired
    CustomEntityValidator<AppUser> musicEntityValidator;

    @BeforeEach
    void beforeAllTests() {
        this.musicEntityValidator = new CustomEntityValidator<>();
    }

    @Test
    void givenValidEntityWhenValidateEntityShouldReturnEmptyList() {

        AppUser validAppUser = AppUser
                .builder()
                .username("test")
                .password("password")
                .build();

        List<String> testValidEntity = this.musicEntityValidator.validateEntity(validAppUser);

        assertEquals(0, testValidEntity.size());

    }

    @Test
    void givenInvalidEntityWhenValidateEntityShouldReturnErrorList() {

        AppUser invalidAppUser = AppUser
                .builder()
                .username("test")
                .build();

        List<String> testInvalidEntity = this.musicEntityValidator.validateEntity(invalidAppUser);

        assertTrue(testInvalidEntity.size() > 0);

    }
}