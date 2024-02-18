package com.medialistmaker.appuser.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenServiceTest {

    private static final String TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiZXhwIjoxNzA4Mjc1NzY4LCJpYXQiOjE3MDgyNTc3Njh9.Ye5E2CAVHd1cVQqmCaFP1j32Ln_9Ai1vVG9-xw2YdTsoUpBmlv07lCyzeUiA3HfqM9Xd5LtpYXgSC9v8IqVnoA";

    private JwtTokenService tokenService;

    @BeforeEach
    void beforeAllTests() {
        this.tokenService = new JwtTokenService();
    }

    @Test
    void givenTokenWhenGetExpirationDateShouldReturnExpirationDate() throws Exception {

        Date testDate = this.tokenService.getTokenExpirationDate(TOKEN);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        Date realDate = dateFormat.parse("18/02/2024 18:02:48");
        assertEquals(realDate, testDate);

    }

    @Test
    void givenClaimsAndSubjectWhenGenerateTokenShouldGenerateToken() {

        String token = this.tokenService.generateToken(new HashMap<>(), "username");

        assertNotNull(this.tokenService.getTokenExpirationDate(token));

    }
}