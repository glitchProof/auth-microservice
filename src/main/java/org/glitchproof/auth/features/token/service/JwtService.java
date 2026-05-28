package org.glitchproof.auth.features.token.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import java.util.function.Function;
import java.nio.charset.StandardCharsets;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Integer jwtExpiration;

    private final Integer jwtRefreshTokenMultiplier = 10;

    public String getSubjectFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public String generateToken(
            String email,
            Map<String, String> claims,
            Integer expirationTime
    ){
        return Jwts.builder()
                .subject(email)
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSecretKey())
                .compact();

    }

    // Access Token
    public String generateAccessToken(String email){
        return generateToken(email, null, jwtExpiration);
    }

    public String generateAccessToken(String email, Map<String, String> claims){
        return generateToken(email, claims, jwtExpiration);
    }

    // Refresh token
    public String generateRefreshToken(String email){
        return generateToken(email, null, jwtExpiration * jwtRefreshTokenMultiplier);
    }

    public String generateRefreshToken(String email, Map<String, String> claims){
        return generateToken(email, claims, jwtExpiration * jwtRefreshTokenMultiplier);
    }

    // Validation
    public Boolean validate(String token) {
        return !isTokenExpired(token);
    }

    public Boolean isTokenExpired(String token) {
        return getClaim(token, Claims::getExpiration).before(new Date());
    }


    // Utils
    private SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
    }

    private <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getClaimsFromToken(token);

        return claimsResolver.apply(claims);
    }
}
