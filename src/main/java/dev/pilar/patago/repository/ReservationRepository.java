package dev.pilar.patago.repository;

import dev.pilar.patago.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    boolean existsByDogNameAndUserUsername(String dogName, String username);
}