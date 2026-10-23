package ru.chernyshev.myeighthtestappspringboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.chernyshev.myeighthtestappspringboot.model.Location;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findAllByOrderByIdAsc();
    List<Location> findByCreatedByOrderByIdAsc(String createdBy);
}
