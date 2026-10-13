package org.kash.jwtsecurityapi.service;

import org.kash.jwtsecurityapi.dto.LoginRequest;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service 
@AllArgsConstructor 
public class AuthService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

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
}
