package ru.chernyshev.myeighthtestappspringboot.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.chernyshev.myeighthtestappspringboot.model.AppUser;
import ru.chernyshev.myeighthtestappspringboot.model.Role;
import ru.chernyshev.myeighthtestappspringboot.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger actions = LoggerFactory.getLogger("user.actions");

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean exists(String username) {
        return userRepository.existsByUsername(username);
    }

    public List<AppUser> findAll() {
        return userRepository.findAll(Sort.by("username"));
    }

    /** Регистрация: всегда роль READ_ONLY. */
    @Transactional
    public AppUser register(String username, String rawPassword) {
        AppUser u = new AppUser();
        u.setUsername(username.trim());
        u.setPassword(passwordEncoder.encode(rawPassword));
        u.setRole(Role.READ_ONLY);
        userRepository.save(u);
        actions.info("user={} action=REGISTER role={}", u.getUsername(), u.getRole());
        return u;
    }

    @Transactional
    public AppUser create(String username, String rawPassword, Role role) {
        AppUser u = new AppUser();
        u.setUsername(username);
        u.setPassword(passwordEncoder.encode(rawPassword));
        u.setRole(role);
        return userRepository.save(u);
    }

    @Transactional
    public void changeRole(String username, Role role, String actor) {
        if (username.equals(actor)) {
            throw new IllegalStateException("Нельзя менять собственную роль (чтобы не потерять доступ администратора)");
        }
        AppUser u = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: " + username));
        Role old = u.getRole();
        u.setRole(role);
        userRepository.save(u);
        actions.info("user={} action=CHANGE_ROLE target={} {}->{}", actor, username, old, role);
    }
}
