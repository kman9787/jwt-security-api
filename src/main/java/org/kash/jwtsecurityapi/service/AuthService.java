package org.kash.jwtsecurityapi.service;

import java.security.SignatureException;

import org.kash.jwtsecurityapi.dto.LoginRequest;
import org.kash.jwtsecurityapi.dto.RefreshTokenRequest;
import org.kash.jwtsecurityapi.dto.RegisterUserRequest;
import org.kash.jwtsecurityapi.dto.TokenPair;
import org.kash.jwtsecurityapi.model.AppUser;
import org.kash.jwtsecurityapi.model.UserRole;
import org.kash.jwtsecurityapi.repository.AppUserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@Service 
@AllArgsConstructor 
public class AuthService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService detailsService;

    @Transactional 
    public void registerUser(RegisterUserRequest registerRequest) throws IllegalArgumentException{
        if(appUserRepository.existsByUserName(registerRequest.username())){
            throw new IllegalArgumentException("User name already exists.");
        }

        if(registerRequest.role() == UserRole.ROLE_ADMIN){
            throw new IllegalArgumentException("Admin already exists.");
        }

        var user = AppUser.builder()
            .fullName(registerRequest.fullname())
            .userName(registerRequest.username())
            .password(passwordEncoder.encode(registerRequest.password()))
            .role(registerRequest.role())
            .build();

        appUserRepository.save(user);
    }

    public TokenPair login(LoginRequest loginRequest) throws BadCredentialsException, 
            DisabledException, LockedException, AuthenticationException{
        var authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtService.generateTokenPair(authentication);
    }

    public TokenPair refreshToken(RefreshTokenRequest request) throws 
            ExpiredJwtException, MalformedJwtException, UnsupportedJwtException, 
            SignatureException, IllegalArgumentException, Exception{
                
        var refreshToken = request.refreshToken();
        if(!jwtService.isRefreshToken(refreshToken)){
            throw new IllegalArgumentException("Invalid refresh token");
        }

        var username = jwtService.extractUserNameFromToken(refreshToken);
        UserDetails userDetails = detailsService.loadUserByUsername(username);

        if(userDetails == null){
            throw new IllegalArgumentException("User not found");
        }

        var authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getAuthorities());
        var accessToken = jwtService.generateAccessToken(authentication);
        return new TokenPair(accessToken, refreshToken);
    }
}
