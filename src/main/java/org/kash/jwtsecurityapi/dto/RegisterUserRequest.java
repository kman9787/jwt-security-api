package org.kash.jwtsecurityapi.dto;

import org.kash.jwtsecurityapi.model.UserRole;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest (

    @NotBlank(message = "Full name must not be empty")
    @Size(min = 3, max = 50, message = "Full name must be 3 to 50 characters long")
    String fullname,

    @NotBlank(message = "User name is required")
    @Size(min =3, max = 50, message = "User name must be 3 to 50 characters long")
    String username,

    @NotBlank(message = "Password is required")
    @Size(min =3, message = "Password mut be atleast 3 characters ")
    String password,

    @NotNull(message = "Role is required")
    UserRole role
){}

