package dev.pilar.patago.repository;

import dev.pilar.patago.model.Dog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DogRepository extends JpaRepository<Dog, Long> {
    boolean existsByName(String name);
    Optional<Dog> findByName(String name);
}