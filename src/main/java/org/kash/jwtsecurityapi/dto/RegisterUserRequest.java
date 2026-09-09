package org.kash.jwtsecurityapi.dto;

import org.kash.jwtsecurityapi.model.UserRole;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class RegisterUserRequest {

    @NotBlank(message = "Full name must not be empty")
    @Size(min = 3, max = 50, message = "Full name must be 3 to 50 characters long")
    private String fullName;

    @NotBlank(message = "User name is required")
    @Size(min =3, max = 50, message = "User name must be 3 to 50 characters long")
    private String userName;

    @NotBlank(message = "Password is required")
    @Size(min =3, message = "Password mut be atleast 3 characters ")
    private String password;

    private UserRole role;
}
