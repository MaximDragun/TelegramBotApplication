package org.example.repository;

import org.example.model.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ApplicationUserRepository extends JpaRepository<ApplicationUser,Long> {
    Optional<ApplicationUser> findApplicationUserByTelegramUserId(Long id);
}
