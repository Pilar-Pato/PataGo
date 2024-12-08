package dev.pilar.patago.service;

import dev.pilar.patago.model.Role;
import dev.pilar.patago.model.User;
import dev.pilar.patago.repository.RoleRepository;
import dev.pilar.patago.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Crear un nuevo usuario con roles
    public User createUser(User user) {
        // Encriptar la contraseña antes de guardarla
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Asignar rol USER al nuevo usuario
        Role userRole = roleRepository.findByName("ROLE_USER");
        if (userRole == null) {
            userRole = new Role("ROLE_USER");
            roleRepository.save(userRole);
        }

        // Asignar el rol al usuario
        user.addRole(userRole);

        // Guardar el usuario en la base de datos
        return userRepository.save(user);
    }

    // Crear un nuevo usuario admin con roles
    public User createAdminUser(User user) {
        // Encriptar la contraseña antes de guardarla
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Asignar rol ADMIN al nuevo usuario
        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        if (adminRole == null) {
            adminRole = new Role("ROLE_ADMIN");
            roleRepository.save(adminRole);
        }

        // Asignar el rol ADMIN al usuario
        user.addRole(adminRole);

        // Guardar el usuario en la base de datos
        return userRepository.save(user);
    }

    // Implementar la carga de detalles de usuario (para autenticación)
    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new org.springframework.security.core.userdetails.UsernameNotFoundException("Usuario no encontrado: " + username));

        // Devolver los detalles del usuario con roles
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles().stream().map(role -> role.getName()).toArray(String[]::new))
                .build();
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Método para obtener un usuario por su ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Método para guardar o actualizar un usuario
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Método para eliminar un usuario por su ID
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}
