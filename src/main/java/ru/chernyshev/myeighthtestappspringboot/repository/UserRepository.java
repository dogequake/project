package ru.chernyshev.myeighthtestappspringboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.chernyshev.myeighthtestappspringboot.model.AppUser;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    boolean existsByUsername(String username);
}
