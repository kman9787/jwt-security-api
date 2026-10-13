package org.kash.jwtsecurityapi.controller;

import java.security.SignatureException;

import org.kash.jwtsecurityapi.dto.LoginRequest;
import org.kash.jwtsecurityapi.dto.RefreshTokenRequest;
import org.kash.jwtsecurityapi.dto.RegisterUserRequest;
import org.kash.jwtsecurityapi.dto.TokenPair;
import org.kash.jwtsecurityapi.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;

@Slf4j 
@RestController 
@RequestMapping("/api/auth")
@RequiredArgsConstructor  
public class AuthController{

    private final AuthService authService;

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerUset(@Valid @RequestBody RegisterUserRequest request){
        try{
            authService.registerUser(request);
            return ResponseEntity.ok("Success.");
        } catch (IllegalArgumentException ie){
            log.error("Failed to register user: {}");
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Failed.");
        }
    }

    @PostMapping("/usr/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try{
            var tokenPair = authService.login(loginRequest);
            return ResponseEntity.ok(tokenPair);
        } catch (BadCredentialsException e) {
            log.error("Invalid username or password: {}", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        } catch (DisabledException e) {
            log.error("Account has been disabled: {}", e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("This account has been disabled.");
        } catch (LockedException e) {
            log.error("Account has been locked: {}", e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("This account has been locked.");
        } catch (Exception e) {
            log.error("An error occurred during login: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during login.");
        }
    }
    
    @PostMapping("/usr/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        
        try{

            TokenPair tokenPair = authService.refreshToken(request);
            return ResponseEntity.ok(tokenPair);
        } catch(SignatureException e) {
            log.error("Invalid JWT Sigature: {}", e.getMessage(), e);
        } catch(MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage(), e);
        } catch(ExpiredJwtException e) {
            log.error("JWT token expired: {}", e.getMessage(), e);
        } catch(UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage(), e);
        } catch(IllegalArgumentException e) {
            log.error("JWT claims is empty: {}", e.getMessage(), e);
        } catch(Exception e) {
            log.error("JWT geneic exeption: {}", e.getMessage(), e);
        }
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during login.");
    }
    
}
  