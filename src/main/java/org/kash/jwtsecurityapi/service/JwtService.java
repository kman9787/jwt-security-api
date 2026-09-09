package org.kash.jwtsecurityapi.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service 
public class JwtService {

    @Value("${app.jwt.secret}")
    private String jwString;

    @Value("${app.jwt.expiration}")
    private long jwtExpirationMs;

    @Value("${app.jwt.refresh-expiration}")
    private long refreshExpirationMs;

    private static final String TOKEN_PREFIX = "Bearer";
}
