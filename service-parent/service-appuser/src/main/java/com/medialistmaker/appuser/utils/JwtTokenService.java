package com.medialistmaker.appuser.utils;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenService {

    private static final Long JWT_TOKEN_DURATION = 5L * 60L * 60L;

    private static final String SECRET_KEY = "test";

    public Date getTokenExpirationDate(String token) {
        return this.getClaimFromToken(token, Claims::getExpiration);
    }

    public String generateToken(Map<String, Object> claims, String subject) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_DURATION * 1000))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimResolver) {
        Claims claims = this.getAllClaimsFromToken(token);
        return claimResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts
                .parser()
                .setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody();
    }

}
