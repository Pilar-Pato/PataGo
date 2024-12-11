package dev.pilar.patago.controller;

import dev.pilar.patago.model.Reservation;
import dev.pilar.patago.model.Dog;
import dev.pilar.patago.model.User;
import dev.pilar.patago.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationControllerTest {

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationController reservationController;

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
        when(reservationService.getAllReservations()).thenReturn(reservations);

        List<Reservation> result = reservationController.getAllReservations();

        assertEquals(1, result.size());
        assertEquals(testReservation, result.get(0));
        verify(reservationService, times(1)).getAllReservations();
    }

    @Test
    void getReservationById_ExistingReservation() {
        when(reservationService.getReservationById(1L)).thenReturn(Optional.of(testReservation));

        ResponseEntity<Reservation> response = reservationController.getReservationById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testReservation, response.getBody());
        verify(reservationService, times(1)).getReservationById(1L);
    }

    @Test
    void getReservationById_NonExistingReservation() {
        when(reservationService.getReservationById(2L)).thenReturn(Optional.empty());

        ResponseEntity<Reservation> response = reservationController.getReservationById(2L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(reservationService, times(1)).getReservationById(2L);
    }

    @Test
    void createReservation() {
        when(reservationService.saveReservation(any(Reservation.class))).thenReturn(testReservation);

        ResponseEntity<Reservation> response = reservationController.createReservation(testReservation);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testReservation, response.getBody());
        verify(reservationService, times(1)).saveReservation(testReservation);
    }

    @Test
    void updateReservation_ExistingReservation() {
        Reservation updatedReservation = new Reservation(testDog, testUser, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), "Confirmed");
        when(reservationService.getReservationById(1L)).thenReturn(Optional.of(testReservation));
        when(reservationService.saveReservation(any(Reservation.class))).thenReturn(updatedReservation);

        ResponseEntity<Reservation> response = reservationController.updateReservation(1L, updatedReservation);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedReservation, response.getBody());
        assertEquals(1L, updatedReservation.getId());
        verify(reservationService, times(1)).getReservationById(1L);
        verify(reservationService, times(1)).saveReservation(updatedReservation);
    }

    @Test
    void updateReservation_NonExistingReservation() {
        when(reservationService.getReservationById(2L)).thenReturn(Optional.empty());

        ResponseEntity<Reservation> response = reservationController.updateReservation(2L, testReservation);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(reservationService, times(1)).getReservationById(2L);
        verify(reservationService, never()).saveReservation(any(Reservation.class));
    }

    @Test
    void deleteReservation() {
        ResponseEntity<Void> response = reservationController.deleteReservation(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(reservationService, times(1)).deleteReservation(1L);
    }
}