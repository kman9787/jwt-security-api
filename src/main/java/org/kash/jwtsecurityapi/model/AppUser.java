package org.kash.jwtsecurityapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Entity 
@Table(name = "app_user")
public class AppUser {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    @NotBlank(message = "Full name must not be empty")
    @Size(min = 3, max = 50, message = "Full name must be 3 to 50 characters long")
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @NotBlank(message = "User name is required")
    @Size(min =3, max = 50, message = "User name must be 3 to 50 characters long")
    @Column(name = "user_name", unique = true)
    private String userName;

    @NotBlank(message = "Password is required")
    @Size(min =3, message = "Password mut be atleast 3 characters ")
    @Column(name = "password", nullable = false )
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public AppUser(
            @NotBlank(message = "Full name must not be empty") @Size(min = 3, max = 50, message = "Full name must be 3 to 50 characters long") String fullName,
            @NotBlank(message = "User name is required") @Size(min = 3, max = 50, message = "User name must be 3 to 50 characters long") String userName,
            @NotBlank(message = "Password is required") @Size(min = 3, message = "Password mut be atleast 3 characters ") String password,
            UserRole role) {
        this.fullName = fullName;
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    
}
 