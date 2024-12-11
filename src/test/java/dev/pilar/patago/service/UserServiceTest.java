package dev.pilar.patago.service;

import dev.pilar.patago.model.Role;
import dev.pilar.patago.model.User;
import dev.pilar.patago.repository.RoleRepository;
import dev.pilar.patago.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private Role testRole;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testRole = new Role("USER");
        testRole.setId(1L);
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.addRole(testRole);
    }

    @Test
    void createUser() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(roleRepository.findByName("USER")).thenReturn(testRole);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.createUser(testUser, "USER");

        assertNotNull(result);
        assertEquals("encodedPassword", result.getPassword());
        assertTrue(result.getRoles().contains(testRole));
        verify(userRepository).save(testUser);
    }

    @Test
    void createUser_NewRole() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(roleRepository.findByName("NEW_ROLE")).thenReturn(null);
        when(roleRepository.save(any(Role.class))).thenReturn(new Role("NEW_ROLE"));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.createUser(testUser, "NEW_ROLE");

        assertNotNull(result);
        assertEquals("encodedPassword", result.getPassword());
        verify(roleRepository).save(any(Role.class));
        verify(userRepository).save(testUser);
    }

    @Test
    void loadUserByUsername() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = userService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void loadUserByUsername_UserNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("nonexistent");
        });
    }

    @Test
    void getAllUsers() {
        List<User> users = Arrays.asList(testUser);
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals(testUser, result.get(0));
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_ExistingUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        Optional<User> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_NonExistingUser() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserById(2L);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(2L);
    }

    @Test
    void saveUser() {
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.saveUser(testUser);

        assertEquals(testUser, result);
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void deleteUser() {
        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}