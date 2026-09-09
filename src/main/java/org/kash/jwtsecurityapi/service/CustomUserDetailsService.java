package org.kash.jwtsecurityapi.service;

import java.nio.file.attribute.UserPrincipal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.kash.jwtsecurityapi.model.AppUser;
import org.kash.jwtsecurityapi.repository.AppUserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class CustomUserDetailsService implements UserDetailsService {

    private final  AppUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findByUserName(username).orElseThrow(() -> new UsernameNotFoundException("Authentication failed"));
        return new User(
            appUser.getUserName(),
            appUser.getPassword(),
            getAuthority(appUser)
        );
    }

    private Collection<? extends GrantedAuthority> getAuthority(AppUser appUser) {
        GrantedAuthority authority = new SimpleGrantedAuthority(appUser.getRole().name());
        return List.of(authority);
    }

} 
