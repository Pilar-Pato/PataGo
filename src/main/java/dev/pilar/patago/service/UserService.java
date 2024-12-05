package dev.pilar.patago.service;

import dev.pilar.patago.model.Role;
import dev.pilar.patago.model.User;
import dev.pilar.patago.repository.RoleRepository;
import dev.pilar.patago.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void createUser() {
        // Crear roles
        Role userRole = roleRepository.findByName("ROLE_USER");
        if (userRole == null) {
            userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
        }

        // Crear un nuevo usuario
        User user = new User();
        user.setUsername("Pilar_pato");  // Nombre de usuario
        user.setPassword(passwordEncoder.encode("1234"));  // Contraseña encriptada
        user.setName("Pilar Pato");  // Nombre completo
        user.setEmail("pilar.pato@example.com");  // Correo electrónico
        
        // Asignar roles al usuario
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);
    }
}
