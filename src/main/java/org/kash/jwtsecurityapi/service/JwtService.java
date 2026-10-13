package org.kash.jwtsecurityapi.service;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.security.SignatureException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.crypto.SecretKey;

import org.kash.jwtsecurityapi.dto.TokenPair;
import org.kash.jwtsecurityapi.enums.TokenType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

@Service 
@Slf4j 
public class JwtService {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private long jwtExpirationMs;

    @Value("${app.jwt.refresh-expiration}")
    private long refreshExpirationMs;

    private static final String TOKEN_PREFIX = "Bearer";

    // Generate access token
    public String generateAccessToken(Authentication authentication){
        return generateToken(authentication, jwtExpirationMs, new HashMap<>());
    }

    // Generate the refresh token
    public String generateRefreshToken(Authentication authentication){
        Map<String, String> claims = new HashMap<>();
        claims.put("tokenType", TokenType.REFRESH.name());

        return generateToken(authentication, refreshExpirationMs, claims);
    }

    // Validate the token
    public boolean isValidTokenForUser(String token, UserDetails userDetails) throws ExpiredJwtException, 
            MalformedJwtException, UnsupportedJwtException, SignatureException, IllegalArgumentException, 
            Exception{

        final var userName = extractUserNameFromToken(token);
        if(Objects.equals(userName, userDetails.getUsername()))
            return true;
       
        return false;
    }

    public boolean isValidToken(String token) throws ExpiredJwtException, 
            MalformedJwtException, UnsupportedJwtException, SignatureException, IllegalArgumentException, 
            Exception{

        var claims = getClaims(token);
        if(claims != null)
            return true;
       
        return false;
    }

    // validate if the token is refresh token
    public boolean isRefreshToken(String token) throws 
            ExpiredJwtException, MalformedJwtException, UnsupportedJwtException, 
            SignatureException, IllegalArgumentException, Exception{

        var claims = getClaims(token);
        if(claims == null) return false;

        return TokenType.REFRESH.name().equals(claims.get("tokenType"));
    }

    public boolean isAccessToken(String token) throws Exception, SignatureException, 
        MalformedJwtException, ExpiredJwtException, UnsupportedJwtException, IllegalArgumentException{

        var claims = getClaims(token);
        if(claims == null) return false;

        return TokenType.ACCESS.name().equals(claims.get("tokenType"));
    }

    public boolean isAccessRefresh(String token) throws Exception, SignatureException, 
        MalformedJwtException, ExpiredJwtException, UnsupportedJwtException, IllegalArgumentException{

        var claims = getClaims(token);
        if(claims == null) return false;

        return TokenType.REFRESH.name().equals(claims.get("tokenType"));
    }

    public String extractUserNameFromToken(String token) throws Exception, SignatureException, 
            MalformedJwtException, ExpiredJwtException, UnsupportedJwtException, IllegalArgumentException{
        
        var claims = getClaims(token);
        if(claims != null) 
            return claims.getSubject(); 

        return null;
    }

    private Claims getClaims(String token) throws Exception, SignatureException, 
        MalformedJwtException, ExpiredJwtException, UnsupportedJwtException, IllegalArgumentException{

        return Jwts.parser()
            .verifyWith(getSignInKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    private String generateToken(Authentication authentication, long expirationMs, 
            Map<String, String> claims){
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        Date dateNow = new Date(System.currentTimeMillis());
        Date dateExpired = new Date(dateNow.getTime() + expirationMs);

        return Jwts.builder()
            .header().add("type", "JWT").and()
            .subject(userPrincipal.getUsername())
            .claims(claims)
            .issuedAt(dateNow)
            .expiration(dateExpired)
            .signWith(getSignInKey())
            .compact();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenPair generateTokenPair(Authentication authentication) {
        var accessToken = generateAccessToken(authentication);
        var refreshToken = generateRefreshToken(authentication);
        return new TokenPair(accessToken, refreshToken);
    }
}
