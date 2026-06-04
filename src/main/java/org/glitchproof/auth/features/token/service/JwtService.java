package org.glitchproof.auth.features.token.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.glitchproof.auth.features.auth.enums.TokenType;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.glitchproof.auth.features.token.dto.TokenResponse;

import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.access-token-expiration}")
    private Long jwtAccessExpireTime;

    @Value("${app.jwt.refresh-token-expiration}")
    private Long jwtRefreshExpireTime;

    public String getSubjectFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public String generateToken(
            String email,
            Map<String, String> claims,
            Long expirationTime,
            TokenType type
    ){
        return Jwts.builder()
                .subject(email)
                .claims(claims)
                .claim("type", type)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSecretKey())
                .compact();

    }

    // Access Token
    public String generateAccessToken(String email){
        return generateToken(email, null, jwtAccessExpireTime, TokenType.ACCESS);
    }

    public String generateAccessToken(String email, Map<String, String> claims){
        return generateToken(email, claims, jwtAccessExpireTime, TokenType.ACCESS);
    }

    // Refresh token
    public String generateRefreshToken(String email){
        return generateToken(email, null, jwtRefreshExpireTime, TokenType.REFRESH);
    }

    public String generateRefreshToken(String email, Map<String, String> claims){
        return generateToken(email, claims, jwtRefreshExpireTime, TokenType.REFRESH);
    }

    public TokenResponse generatePairToken(String email){
        return new TokenResponse(generateAccessToken(email), generateRefreshToken(email));
    }

    // Validation
    public Boolean validate(String token) {
        return !isTokenExpired(token);
    }

    public Boolean validate(String token, TokenType tokenType) {
        return !isTokenExpired(token) && tokenType.equals(getTokenType(token));
    }

    public Boolean isTokenExpired(String token) {
        return getClaim(token, Claims::getExpiration).before(new Date());
    }

    // Utils
    private SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
    }

    private <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getClaimsFromToken(token);


        return claimsResolver.apply(claims);
    }

    public TokenType getTokenType(String token) {
        return getClaim(token, claims -> {
            String type = claims.get("type", String.class);

            return TokenType.valueOf(type);
        });
    }
}
