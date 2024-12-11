package dev.pilar.patago.controller;

import dev.pilar.patago.model.Dog;
import dev.pilar.patago.model.User;
import dev.pilar.patago.service.DogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DogControllerTest {

    @Mock
    private DogService dogService;

    @InjectMocks
    private DogController dogController;

    private Dog testDog;
    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User();
        testUser.setId(1L);
        testDog = new Dog("Buddy", "Labrador", 3, "Medium", "Friendly", testUser);
        testDog.setId(1L);
    }

    @Test
    void getAllDogs() {
        List<Dog> dogs = Arrays.asList(testDog);
        when(dogService.getAllDogs()).thenReturn(dogs);

        List<Dog> result = dogController.getAllDogs();

        assertEquals(1, result.size());
        assertEquals(testDog, result.get(0));
        verify(dogService, times(1)).getAllDogs();
    }

    @Test
    void getDogById_ExistingDog() {
        when(dogService.getDogById(1L)).thenReturn(Optional.of(testDog));

        ResponseEntity<Dog> response = dogController.getDogById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testDog, response.getBody());
        verify(dogService, times(1)).getDogById(1L);
    }

    @Test
    void getDogById_NonExistingDog() {
        when(dogService.getDogById(2L)).thenReturn(Optional.empty());

        ResponseEntity<Dog> response = dogController.getDogById(2L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(dogService, times(1)).getDogById(2L);
    }

    @Test
    void createDog() {
        when(dogService.saveDog(any(Dog.class))).thenReturn(testDog);

        ResponseEntity<Dog> response = dogController.createDog(testDog);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testDog, response.getBody());
        verify(dogService, times(1)).saveDog(testDog);
    }

    @Test
    void updateDog_ExistingDog() {
        Dog updatedDog = new Dog("Max", "Golden Retriever", 4, "Large", "Playful", testUser);
        when(dogService.getDogById(1L)).thenReturn(Optional.of(testDog));
        when(dogService.saveDog(any(Dog.class))).thenReturn(updatedDog);

        ResponseEntity<Dog> response = dogController.updateDog(1L, updatedDog);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedDog, response.getBody());
        assertEquals(1L, updatedDog.getId());
        verify(dogService, times(1)).getDogById(1L);
        verify(dogService, times(1)).saveDog(updatedDog);
    }

    @Test
    void updateDog_NonExistingDog() {
        when(dogService.getDogById(2L)).thenReturn(Optional.empty());

        ResponseEntity<Dog> response = dogController.updateDog(2L, testDog);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(dogService, times(1)).getDogById(2L);
        verify(dogService, never()).saveDog(any(Dog.class));
    }

    @Test
    void deleteDog() {
        ResponseEntity<Void> response = dogController.deleteDog(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(dogService, times(1)).deleteDog(1L);
    }
}