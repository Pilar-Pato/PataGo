package dev.pilar.patago.repository;

import dev.pilar.patago.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // Métodos personalizados si son necesarios
}
