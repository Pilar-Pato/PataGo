package dev.pilar.patago.service;

import dev.pilar.patago.model.Reservation;
import dev.pilar.patago.model.Dog;
import dev.pilar.patago.model.User;
import dev.pilar.patago.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Reservation testReservation;
    private Dog testDog;
    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User();
        testUser.setId(1L);
        testDog = new Dog("Buddy", "Labrador", 3, "Medium", "Friendly", testUser);
        testDog.setId(1L);
        testReservation = new Reservation(testDog, testUser, LocalDateTime.now(), LocalDateTime.now().plusDays(1), "Pending");
        testReservation.setId(1L);
    }

    @Test
    void getAllReservations() {
        List<Reservation> reservations = Arrays.asList(testReservation);
        when(reservationRepository.findAll()).thenReturn(reservations);

        List<Reservation> result = reservationService.getAllReservations();

        assertEquals(1, result.size());
        assertEquals(testReservation, result.get(0));
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void getReservationById_ExistingReservation() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(testReservation));

        Optional<Reservation> result = reservationService.getReservationById(1L);

        assertTrue(result.isPresent());
        assertEquals(testReservation, result.get());
        verify(reservationRepository, times(1)).findById(1L);
    }

    @Test
    void getReservationById_NonExistingReservation() {
        when(reservationRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Reservation> result = reservationService.getReservationById(2L);

        assertFalse(result.isPresent());
        verify(reservationRepository, times(1)).findById(2L);
    }

    @Test
    void saveReservation() {
        when(reservationRepository.save(any(Reservation.class))).thenReturn(testReservation);

        Reservation result = reservationService.saveReservation(testReservation);

        assertEquals(testReservation, result);
        verify(reservationRepository, times(1)).save(testReservation);
    }

    @Test
    void deleteReservation() {
        reservationService.deleteReservation(1L);

        verify(reservationRepository, times(1)).deleteById(1L);
    }
}