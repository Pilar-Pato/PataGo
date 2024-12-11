package dev.pilar.patago.service;

import dev.pilar.patago.model.Role;
import dev.pilar.patago.repository.RoleRepository;
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

class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    private Role testRole;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testRole = new Role("ROLE_USER");
        testRole.setId(1L);
    }

    @Test
    void getAllRoles() {
        List<Role> roles = Arrays.asList(testRole);
        when(roleRepository.findAll()).thenReturn(roles);

        List<Role> result = roleService.getAllRoles();

        assertEquals(1, result.size());
        assertEquals(testRole, result.get(0));
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void getRoleById_ExistingRole() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(testRole));

        Optional<Role> result = roleService.getRoleById(1L);

        assertTrue(result.isPresent());
        assertEquals(testRole, result.get());
        verify(roleRepository, times(1)).findById(1L);
    }

    @Test
    void getRoleById_NonExistingRole() {
        when(roleRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Role> result = roleService.getRoleById(2L);

        assertFalse(result.isPresent());
        verify(roleRepository, times(1)).findById(2L);
    }

    @Test
    void saveRole() {
        when(roleRepository.save(any(Role.class))).thenReturn(testRole);

        Role result = roleService.saveRole(testRole);

        assertEquals(testRole, result);
        verify(roleRepository, times(1)).save(testRole);
    }

    @Test
    void deleteRole() {
        roleService.deleteRole(1L);

        verify(roleRepository, times(1)).deleteById(1L);
    }
}