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
        
        when(userService.getAllUsers()).thenReturn(List.of(testUser));

        
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())  
                .andExpect(jsonPath("$[0].username").value("john.doe"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetUserById_Found() throws Exception {
        
        when(userService.getUserById(anyLong())).thenReturn(Optional.of(testUser));

        
        mockMvc.perform(get("/users/{id}", 1L))
                .andExpect(status().isOk())  
                .andExpect(jsonPath("$.username").value("john.doe"));
    }

    @Test
void testGetUserById_NotFound() throws Exception {
    
    when(userService.getUserById(anyLong())).thenReturn(Optional.empty());

    
    mockMvc.perform(get("/users/{id}", 1L))
            .andExpect(status().isNonAuthoritativeInformation())  
            .andExpect(jsonPath("$.username").value(""))  
            .andExpect(jsonPath("$.email").value(""))  
            .andExpect(jsonPath("$.name").value(""))  
            .andExpect(jsonPath("$.id").value(0));  
}
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateUser() throws Exception {
        
        when(userService.saveUser(any(User.class))).thenReturn(testUser);

        
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"john.doe\", \"password\": \"password123\", \"name\": \"John Doe\", \"email\": \"john.doe@example.com\"}"))
                .andExpect(status().isOk())  
                .andExpect(jsonPath("$.username").value("john.doe"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateUser_Found() throws Exception {
        
        when(userService.getUserById(anyLong())).thenReturn(Optional.of(testUser));
        when(userService.saveUser(any(User.class))).thenReturn(testUser);

        
        mockMvc.perform(put("/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"john.doe\", \"password\": \"newpassword\", \"name\": \"John Doe\", \"email\": \"john.doe@example.com\"}"))
                .andExpect(status().isOk())  
                .andExpect(jsonPath("$.username").value("john.doe"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateUser_NotFound() throws Exception {
        
        when(userService.getUserById(anyLong())).thenReturn(Optional.empty());

        
        mockMvc.perform(put("/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"john.doe\", \"password\": \"newpassword\", \"name\": \"John Doe\", \"email\": \"john.doe@example.com\"}"))
                .andExpect(status().isNotFound());  
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteUser() throws Exception {
        
        doNothing().when(userService).deleteUser(anyLong());

        
        mockMvc.perform(delete("/users/{id}", 1L))
                .andExpect(status().isNoContent());  
    }

    }
