package org.kash.jwtsecurityapi.repository;

import java.util.Optional;

import org.kash.jwtsecurityapi.model.AppUser;
import org.kash.jwtsecurityapi.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AppUserRepository extends JpaRepository<AppUser, Long>{
    public Optional<AppUser> findByUserName(String userName);
    public Boolean existsByUserName(String userName);
    public void deleteByRole(UserRole userRole);
}
