package ru.chernyshev.myeighthtestappspringboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.chernyshev.myeighthtestappspringboot.model.Server;

import java.util.List;

public interface ServerRepository extends JpaRepository<Server, Long> {
    List<Server> findAllByOrderByIdAsc();
    List<Server> findByCreatedByOrderByIdAsc(String createdBy);
    boolean existsByLocationId(Long locationId);
}
