package dev.pilar.patago.service;

import dev.pilar.patago.model.Dog;
import dev.pilar.patago.model.User;
import dev.pilar.patago.repository.DogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DogServiceTest {

    @Mock
    private DogRepository dogRepository;

    @InjectMocks
    private DogService dogService;

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
        when(dogRepository.findAll()).thenReturn(dogs);

        List<Dog> result = dogService.getAllDogs();

        assertEquals(1, result.size());
        assertEquals(testDog, result.get(0));
        verify(dogRepository, times(1)).findAll();
    }

    @Test
    void getDogById_ExistingDog() {
        when(dogRepository.findById(1L)).thenReturn(Optional.of(testDog));

        Optional<Dog> result = dogService.getDogById(1L);

        assertTrue(result.isPresent());
        assertEquals(testDog, result.get());
        verify(dogRepository, times(1)).findById(1L);
    }

    @Test
    void getDogById_NonExistingDog() {
        when(dogRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Dog> result = dogService.getDogById(2L);

        assertFalse(result.isPresent());
        verify(dogRepository, times(1)).findById(2L);
    }

    @Test
    void saveDog() {
        when(dogRepository.save(any(Dog.class))).thenReturn(testDog);

        Dog result = dogService.saveDog(testDog);

        assertEquals(testDog, result);
        verify(dogRepository, times(1)).save(testDog);
    }

    @Test
    void deleteDog() {
        dogService.deleteDog(1L);

        verify(dogRepository, times(1)).deleteById(1L);
    }
}