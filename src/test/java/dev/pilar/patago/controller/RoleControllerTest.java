package dev.pilar.patago.controller;

import dev.pilar.patago.model.Role;
import dev.pilar.patago.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

public class RoleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    private Role testRole;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(roleController).build();
        testRole = new Role("ADMIN");
    }

    // Test para obtener todos los roles
    @Test
    void testGetAllRoles() throws Exception {
        when(roleService.getAllRoles()).thenReturn(List.of(testRole));

        mockMvc.perform(get("/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("ADMIN"));

        verify(roleService, times(1)).getAllRoles();
    }

    // Test para obtener un rol por ID
    @Test
    void testGetRoleById() throws Exception {
        when(roleService.getRoleById(1L)).thenReturn(Optional.of(testRole));

        mockMvc.perform(get("/roles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ADMIN"));

        verify(roleService, times(1)).getRoleById(1L);
    }

    // Test para obtener un rol por ID que no existe
    @Test
    void testGetRoleById_NotFound() throws Exception {
        when(roleService.getRoleById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/roles/1"))
                .andExpect(status().isNotFound());

        verify(roleService, times(1)).getRoleById(1L);
    }

    // Test para crear un nuevo rol
    @Test
    void testCreateRole() throws Exception {
        when(roleService.saveRole(any(Role.class))).thenReturn(testRole);
    
        mockMvc.perform(post("/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"ADMIN\"}"))
                .andExpect(status().isOk())  // Cambia a 200 OK
                .andExpect(jsonPath("$.name").value("ADMIN"));
    
        verify(roleService, times(1)).saveRole(any(Role.class));
    }
    


   
    // Test para actualizar un rol que no existe
    @Test
    void testUpdateRole_NotFound() throws Exception {
        when(roleService.getRoleById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/roles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"USER\"}"))
                .andExpect(status().isNotFound());

        verify(roleService, times(1)).getRoleById(1L);
        verify(roleService, times(0)).saveRole(any(Role.class));
    }

    // Test para eliminar un rol
    @Test
    void testDeleteRole() throws Exception {
        doNothing().when(roleService).deleteRole(1L);

        mockMvc.perform(delete("/roles/1"))
                .andExpect(status().isNoContent());

        verify(roleService, times(1)).deleteRole(1L);
    }

    
}
