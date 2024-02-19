package com.medialistmaker.list.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtTokenService {

    private static final String SECRET_KEY = "test";

    public String getUsername(String token) {
        return this.getClaimFromToken(token, Claims::getSubject);
    }

    public Date getTokenExpirationDate(String token) {
        return this.getClaimFromToken(token, Claims::getExpiration);
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        if(Boolean.FALSE.equals(this.isTokenFormatValid(token))) {
            return Boolean.FALSE;
        }

        String username = this.getUsername(token);

        return username.equals(userDetails.getUsername()) && Boolean.FALSE.equals(this.isTokenExpired(token));
    }

    private Boolean isTokenFormatValid(String token) {

        try {
            this.getAllClaimsFromToken(token);
            return Boolean.TRUE;
        } catch (JwtException e) {
            return Boolean.FALSE;
        }

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

    private Boolean isTokenExpired(String token) {
        Date expirationDate = this.getTokenExpirationDate(token);
        return expirationDate.before(new Date());
    }

}
