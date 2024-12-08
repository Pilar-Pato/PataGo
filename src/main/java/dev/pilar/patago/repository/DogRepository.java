package dev.pilar.patago.repository;

import dev.pilar.patago.model.Dog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DogRepository extends JpaRepository<Dog, Long> {
    // Métodos personalizados si son necesarios
}
