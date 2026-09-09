package org.kash.jwtsecurityapi.service;

import org.kash.jwtsecurityapi.dto.RegisterUserRequest;
import org.kash.jwtsecurityapi.model.AppUser;
import org.kash.jwtsecurityapi.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service 
@AllArgsConstructor 
public class AuthService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional 
    public void registerUser(RegisterUserRequest registerRequest){
        if(appUserRepository.existsByUserName(registerRequest.getUserName())){
            throw new IllegalArgumentException("User name already exists.");
        }

        AppUser user = AppUser.builder()
            .fullName(registerRequest.getFullName())
            .userName(registerRequest.getUserName())
            .password(passwordEncoder.encode(registerRequest.getPassword()))
            .role(registerRequest.getRole())
            .build();

        appUserRepository.save(user);
    }
}
