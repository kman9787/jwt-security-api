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
        claims.put("tokenType", "refresh");

        return generateToken(authentication, refreshExpirationMs, claims);
    }

    // Validate the token
    public boolean isValidToken(String token, UserDetails userDetails) throws ExpiredJwtException, 
            MalformedJwtException, UnsupportedJwtException, SignatureException, IllegalArgumentException, 
            Exception{

        final var userName = extractUserNameFromToken(token);
        if(Objects.equals(userName, userDetails.getUsername()))
            return true;
       
        return false;
    }

    // validate if the token is refresh token
    public boolean isRefreshToken(String token, UserDetails userDetails) throws 
            ExpiredJwtException, MalformedJwtException, UnsupportedJwtException, 
            SignatureException, IllegalArgumentException, Exception{

        var tokenType = getClaims(token).get("tokenType");
        if("refresh".equals(tokenType))
            return true;

        return false;
    }

    public String extractUserNameFromToken(String token) throws Exception, SignatureException, 
        MalformedJwtException, ExpiredJwtException, UnsupportedJwtException, IllegalArgumentException{
        return getClaims(token).getSubject();
    }

    private Claims getClaims(String token) throws Exception, SignatureException, 
        MalformedJwtException, ExpiredJwtException, UnsupportedJwtException, IllegalArgumentException{

        return Jwts.parser()
            .verifyWith(getSignInKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    private String generateToken(Authentication authentication, long expirationMs, Map<String, String> claims){
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        Date dateNow = new Date(System.currentTimeMillis());
        Date dateExpired = new Date(dateNow.getTime() + expirationMs);

        return Jwts.builder()
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
}
