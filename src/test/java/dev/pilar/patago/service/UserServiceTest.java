package dev.pilar.patago.service;

import dev.pilar.patago.model.Role;
import dev.pilar.patago.model.User;
import dev.pilar.patago.repository.RoleRepository;
import dev.pilar.patago.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser_CreatesUserSuccessfully() {
        // Crear datos de prueba para el usuario y rol
        User user = new User();  // Usamos el constructor por defecto
        user.setUsername("john_doe");
        user.setPassword("password123");
        user.setName("John");
        user.setEmail("john@example.com");

        Role role = new Role("USER");

        // Simular comportamiento de las dependencias
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(roleRepository.findByName("USER")).thenReturn(role);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Ejecutar el método que se va a probar
        User createdUser = userService.createUser(user, "USER");

        // Verificaciones
        assertNotNull(createdUser);
        assertEquals("john_doe", createdUser.getUsername());
        assertEquals("encodedPassword", createdUser.getPassword());
        assertTrue(createdUser.getRoles().contains(role));

        // Verificar que las dependencias fueron llamadas correctamente
        verify(passwordEncoder, times(1)).encode("password123");
        verify(roleRepository, times(1)).findByName("USER");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testGetAllUsers_ReturnsListOfUsers() {
        // Crear usuarios de prueba
        User user1 = new User();
        user1.setUsername("john_doe");
        user1.setPassword("password123");
        user1.setName("John");
        user1.setEmail("john@example.com");

        User user2 = new User();
        user2.setUsername("jane_doe");
        user2.setPassword("password456");
        user2.setName("Jane");
        user2.setEmail("jane@example.com");

        // Simulamos la respuesta del repositorio
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        // Ejecutar el método
        List<User> users = userService.getAllUsers();

        // Verificaciones
        assertNotNull(users);
        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));

        // Verificar que se llamó al repositorio
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById_ReturnsUser() {
        // Crear usuario de prueba
        User user = new User();
        user.setUsername("john_doe");
        user.setPassword("password123");
        user.setName("John");
        user.setEmail("john@example.com");

        // Simulamos la respuesta del repositorio
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));

        // Ejecutar el método
        User foundUser = userService.getUserById(1L).orElse(null);

        // Verificaciones
        assertNotNull(foundUser);
        assertEquals("john_doe", foundUser.getUsername());
        assertEquals("password123", foundUser.getPassword());
        assertEquals("John", foundUser.getName());
        assertEquals("john@example.com", foundUser.getEmail());

        // Verificar que se llamó al repositorio
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteUser_DeletesUserSuccessfully() {
        // Crear usuario de prueba
        User user = new User();
        user.setUsername("john_doe");
        user.setPassword("password123");
        user.setName("John");
        user.setEmail("john@example.com");

        // Simulamos que el repositorio lo encuentra y elimina correctamente
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        doNothing().when(userRepository).deleteById(1L);

        // Ejecutar el método
        userService.deleteUser(1L);

        // Verificar que el repositorio fue llamado con el id correcto
        verify(userRepository, times(1)).deleteById(1L);
    }
}
