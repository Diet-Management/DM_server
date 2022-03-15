package com.management.diet.configuration.security.jwt;

import com.management.diet.configuration.security.auth.MyUserDetailsService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SignatureException;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private final MyUserDetailsService memberService;
    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken=request.getHeader("Authorization");
        String refreshToken=request.getHeader("RefreshToken");
        if(accessToken!=null){
            String userEmail=accessTokenExtractEmail(accessToken);
            if(userEmail!=null) registerUserinfoInSecurityContext(userEmail, request);
            if(tokenProvider.isTokenExpired(accessToken) && refreshToken != null){
                String newAccessToken = generateNewAccessToken(refreshToken);
                response.addHeader("JwtToken", newAccessToken);
            }
        }
        filterChain.doFilter(request, response);
    }
    private String accessTokenExtractEmail(String accessToken){
        try{
            return tokenProvider.getUserEmail(accessToken);
        }catch(JwtException | IllegalArgumentException e){
            throw new RuntimeException();
        }
    }
    private void registerUserinfoInSecurityContext(String userEmail, HttpServletRequest req){
        try{
            UserDetails userDetails = memberService.loadUserByUsername(userEmail);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }catch (NullPointerException e){
            throw new RuntimeException();
        }
    }
    private String generateNewAccessToken(String refreshToken){
        try{
            return tokenProvider.generateAccessToken(tokenProvider.getUserEmail(refreshToken));
        }catch (JwtException | IllegalStateException e) {
            throw new RuntimeException();
        }
    }
}
