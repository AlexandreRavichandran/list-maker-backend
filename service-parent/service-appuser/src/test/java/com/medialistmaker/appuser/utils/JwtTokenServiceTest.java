package com.medialistmaker.appuser.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenServiceTest {

    private static final String SECRET_KEY = "test";

    private JwtTokenService tokenService;

    @BeforeEach
    void beforeAllTests() {
        this.tokenService = new JwtTokenService();
    }

    @Test
    void givenTokenWhenGetExpirationDateShouldReturnExpirationDate() throws Exception {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

        Date expirationDate = formatter.parse("01/01/2999 23:59:59");

        String token = Jwts
                .builder()
                .setClaims(new HashMap<>())
                .setSubject("username")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                .compact();

        Date testDate = this.tokenService.getTokenExpirationDate(token);

        assertEquals(expirationDate, testDate);

    }

    @Test
    void givenClaimsAndSubjectWhenGenerateTokenShouldGenerateToken() {

        String token = this.tokenService.generateToken(new HashMap<>(), "username");

        assertNotNull(this.tokenService.getTokenExpirationDate(token));

    }
}