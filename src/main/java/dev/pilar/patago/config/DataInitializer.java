package dev.pilar.patago.config;

import dev.pilar.patago.model.Role;
import dev.pilar.patago.model.User;
import dev.pilar.patago.repository.RoleRepository;
import dev.pilar.patago.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository; // Necesitamos acceder a los roles también

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        try {

            // Verificar si el usuario "root" ya existe
            if (!userRepository.existsByUsername("root")) {

                User root = new User();
                root.setUsername("root");
                root.setPassword(passwordEncoder.encode("1234"));
                root.setName("Root User");
                root.setEmail("root@example.com");

                // Buscar el rol "ADMIN" en la base de datos
                Role adminRole = roleRepository.findByName("ROLE_ADMIN");
                if (adminRole != null) {
                    // Agregar el rol ADMIN al usuario
                    root.addRole(adminRole);
                }

                userRepository.save(root);
                System.out.println("Usuario root creado en la base de datos.");

            } else {
                System.out.println("Usuario root cargado.");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}