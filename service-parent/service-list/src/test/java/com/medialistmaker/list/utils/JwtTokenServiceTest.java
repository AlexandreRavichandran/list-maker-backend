package com.medialistmaker.list.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenServiceTest {

    private JwtTokenService tokenService;

    @BeforeEach
    void beforeAllTests() {
        this.tokenService = new JwtTokenService();
    }

    @Test
    void givenMalFormedTokenWhenIsTokenValidShouldReturnTrue() {

        User user = new User("1", "password", new ArrayList<>());

        Boolean testIsTokenValid = this.tokenService.isTokenValid("test", user);

        assertFalse(testIsTokenValid);
    }

    @Test
    void givenWrongUserTokenWhenIsTokenValidShouldReturnTrue() {

        User user = new User("2", "password", new ArrayList<>());

        Boolean testIsTokenValid = this.tokenService.isTokenValid("test", user);

        assertFalse(testIsTokenValid);
    }

}