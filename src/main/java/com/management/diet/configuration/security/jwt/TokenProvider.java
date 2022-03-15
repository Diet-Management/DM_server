package com.management.diet.configuration.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import io.jsonwebtoken.security.SignatureException;
import java.util.Date;

@Component
public class TokenProvider {
    public static final  long ACCESS_TOKEN_EXPIRED_TIME = 1000*60*30;
    @Value("${jwt.secret}")
    private String secretKey;
    @AllArgsConstructor
    enum TokenType{
        ACCESS_TOKEN("accessToken"),
        ;
        private String value;
    }
    @AllArgsConstructor
    enum TokenClaimName{
        USER_EMAIL("userEmail"),
        TOKEN_TYPE("token_type");
        String value;
    }
    private Key getSigningKey(String secretKey){
        byte keyByte[]=secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyByte);
    }
    public Claims extractAllClaims(String token) throws ExpiredJwtException, IllegalArgumentException, MalformedJwtException, SignatureException,UnsupportedJwtException {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public String getUserEmail(String token){
        return extractAllClaims(token).get(TokenClaimName.USER_EMAIL.value, String.class);
    }
    public String getTokenType(String token) {
        return extractAllClaims(token).get(TokenClaimName.TOKEN_TYPE.value, String.class);
    }
    public Boolean isTokenExpired(String token){
        try{
            extractAllClaims(token).getExpiration();
            return false;
        }catch(ExpiredJwtException e){
            return true;
        }
    }
    private String doGenarateToken(String userEmail, TokenType tokenType, long expireTime){
        final Claims claims = Jwts.claims();
        if (TokenType.ACCESS_TOKEN == tokenType){
            claims.put("userEmail", userEmail);
        }
        claims.put("tokenType", tokenType.value);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+expireTime))
                .signWith(getSigningKey(secretKey), SignatureAlgorithm.HS256)
                .compact();
    }
    public String generateAccessToken(String email){
        return doGenarateToken(email, TokenType.ACCESS_TOKEN, ACCESS_TOKEN_EXPIRED_TIME);
    }
}
