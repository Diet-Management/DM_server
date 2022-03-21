package com.management.diet.configuration.security.jwt;

import com.management.diet.exception.ErrorCode;
import com.management.diet.exception.exception.AccessTokenExpiredException;
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
    public static final  long ACCESS_TOKEN_EXPIRED_TIME = 1000*60*60*3;//accessToken의 만료기간을 3시간으로 설정
    public static long REFRESH_TOKEN_EXPIRED_TIME = ACCESS_TOKEN_EXPIRED_TIME/3 * 24 * 180; // 6달을 refreshToken 만료 기간으로 잡는다.
    @Value("${jwt.secret}")
    private String secretKey;
    @AllArgsConstructor
    enum TokenType{
        ACCESS_TOKEN("accessToken"),
        REFRESH_TOKEN("refreshToken")
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
        if(isTokenExpired(token)){
            throw new AccessTokenExpiredException("AccessToken is expired", ErrorCode.ACCESS_TOKEN_EXPIRED);
        }
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
    private String doGenerateToken(String userEmail, TokenType tokenType, long expireTime){
        final Claims claims = Jwts.claims();
        claims.put("userEmail", userEmail);
        claims.put("tokenType", tokenType.value);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+expireTime))
                .signWith(getSigningKey(secretKey), SignatureAlgorithm.HS256)
                .compact();
    }
    public String generateAccessToken(String email){
        return doGenerateToken(email, TokenType.ACCESS_TOKEN, ACCESS_TOKEN_EXPIRED_TIME);
    }
    public String generateRefreshToken(String email){
        return doGenerateToken(email, TokenType.REFRESH_TOKEN, REFRESH_TOKEN_EXPIRED_TIME);
    }
}
