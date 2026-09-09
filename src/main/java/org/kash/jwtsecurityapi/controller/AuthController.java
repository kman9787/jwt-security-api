package org.kash.jwtsecurityapi.controller;

import org.kash.jwtsecurityapi.dto.RegisterUserRequest;
import org.kash.jwtsecurityapi.model.AppUser;
import org.kash.jwtsecurityapi.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;


@RestController 
@RequestMapping("/api/auth")
@RequiredArgsConstructor  
public class AuthController{

    private final AuthService authService;

    @PostMapping("/register")

    public ResponseEntity<?> registerUset(@Valid @RequestBody RegisterUserRequest request){
        authService.registerUser(request);
        return ResponseEntity.ok("User registered.");
    }
}
  