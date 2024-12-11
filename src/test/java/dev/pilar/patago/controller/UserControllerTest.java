package dev.pilar.patago.controller;

import dev.pilar.patago.model.User;
import dev.pilar.patago.repository.RoleRepository;
import dev.pilar.patago.repository.UserRepository;
import dev.pilar.patago.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserController userController;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("john.doe");
        testUser.setPassword("password123");
        testUser.setName("John Doe");
        testUser.setEmail("john.doe@example.com");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetAllUsers() throws Exception {
        // Configura el comportamiento del servicio
        when(userService.getAllUsers()).thenReturn(List.of(testUser));

        // Realiza la petición GET y verifica el resultado
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())  // Esperamos un 200 OK
                .andExpect(jsonPath("$[0].username").value("john.doe"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetUserById_Found() throws Exception {
        // Configura el comportamiento del servicio
        when(userService.getUserById(anyLong())).thenReturn(Optional.of(testUser));

        // Realiza la petición GET con un ID válido
        mockMvc.perform(get("/users/{id}", 1L))
                .andExpect(status().isOk())  // Esperamos un 200 OK
                .andExpect(jsonPath("$.username").value("john.doe"));
    }

    @Test
void testGetUserById_NotFound() throws Exception {
    // Configura el comportamiento del servicio
    when(userService.getUserById(anyLong())).thenReturn(Optional.empty());

    // Realiza la petición GET con un ID no encontrado
    mockMvc.perform(get("/users/{id}", 1L))
            .andExpect(status().isNonAuthoritativeInformation())  
            .andExpect(jsonPath("$.username").value(""))  // Verificamos que el usuario esté vacío (si es un usuario vacío)
            .andExpect(jsonPath("$.email").value(""))  // Verificamos otros campos del usuario vacío
            .andExpect(jsonPath("$.name").value(""))  // Verificamos otros campos del usuario vacío
            .andExpect(jsonPath("$.id").value(0));  // Verificamos el ID vacío (por ejemplo, 0 si no existe)
}
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateUser() throws Exception {
        // Configura el comportamiento del servicio
        when(userService.saveUser(any(User.class))).thenReturn(testUser);

        // Realiza la petición POST para crear un usuario
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"john.doe\", \"password\": \"password123\", \"name\": \"John Doe\", \"email\": \"john.doe@example.com\"}"))
                .andExpect(status().isOk())  // Ahora debería devolver un 200 OK
                .andExpect(jsonPath("$.username").value("john.doe"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateUser_Found() throws Exception {
        // Configura el comportamiento del servicio
        when(userService.getUserById(anyLong())).thenReturn(Optional.of(testUser));
        when(userService.saveUser(any(User.class))).thenReturn(testUser);

        // Realiza la petición PUT para actualizar un usuario
        mockMvc.perform(put("/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"john.doe\", \"password\": \"newpassword\", \"name\": \"John Doe\", \"email\": \"john.doe@example.com\"}"))
                .andExpect(status().isOk())  // Esperamos un 200 OK
                .andExpect(jsonPath("$.username").value("john.doe"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateUser_NotFound() throws Exception {
        // Configura el comportamiento del servicio
        when(userService.getUserById(anyLong())).thenReturn(Optional.empty());

        // Realiza la petición PUT con un ID no encontrado
        mockMvc.perform(put("/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"john.doe\", \"password\": \"newpassword\", \"name\": \"John Doe\", \"email\": \"john.doe@example.com\"}"))
                .andExpect(status().isNotFound());  // Esperamos un 404 Not Found
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteUser() throws Exception {
        // Configura el comportamiento del servicio
        doNothing().when(userService).deleteUser(anyLong());

        // Realiza la petición DELETE para eliminar un usuario
        mockMvc.perform(delete("/users/{id}", 1L))
                .andExpect(status().isNoContent());  // Ahora debería devolver un 204 No Content
    }

    }
