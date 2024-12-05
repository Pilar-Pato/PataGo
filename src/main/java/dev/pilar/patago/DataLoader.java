package dev.pilar.patago;

import dev.pilar.patago.model.Role;
import dev.pilar.patago.model.User;
import dev.pilar.patago.repository.RoleRepository;
import dev.pilar.patago.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // Crear roles si no existen
        if (roleRepository.count() == 0) {
            Role roleUser = new Role();
            roleUser.setName("ROLE_USER");
            roleRepository.save(roleUser);

            Role roleAdmin = new Role();
            roleAdmin.setName("ROLE_ADMIN");
            roleRepository.save(roleAdmin);

            System.out.println("Roles creados con éxito");
        }

        // Crear un nuevo usuario con roles
        if (userRepository.count() == 0) {
            // Crear el usuario
            User user = new User();
            user.setUsername("Pilar_pato");
            user.setPassword("1234");

            // Asignar múltiples roles
            Role roleUser = roleRepository.findByName("ROLE_USER");
            Role roleAdmin = roleRepository.findByName("ROLE_ADMIN");

            user.getRoles().add(roleUser);  // Agregar el rol de usuario
            user.getRoles().add(roleAdmin); // Agregar el rol de administrador

            userRepository.save(user);

            System.out.println("Usuario 'Pilar_pato' creado con roles");
        }
    }
}
