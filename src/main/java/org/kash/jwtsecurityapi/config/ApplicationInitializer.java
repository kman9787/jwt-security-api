package org.kash.jwtsecurityapi.config;

import org.kash.jwtsecurityapi.model.AppUser;
import org.kash.jwtsecurityapi.model.UserRole;
import org.kash.jwtsecurityapi.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration 
public class ApplicationInitializer {

    @Value("${app.administrator.username}") 
    private String adminUser;

    @Value("${app.administrator.password}") 
    private String adminPassword;

    @Bean 
    CommandLineRunner initAdminUser(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder){
        return args -> {
            if(!appUserRepository.existsByUserName(adminUser)){

                var user = AppUser.builder()
                    .fullName("Administrator")
                    .userName(adminUser)
                    .password(passwordEncoder.encode(adminPassword))
                    .role(UserRole.ROLE_ADMIN)
                    .build();

                appUserRepository.save(user);
            }
        };
    }


}
