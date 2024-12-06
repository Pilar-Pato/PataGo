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
            System.out.println("No existen roles, creando nuevos roles...");

            // Crear el rol de usuario
            Role roleUser = new Role();
            roleUser.setName("ROLE_USER");

            System.out.println("Guardando ROLE_USER...");
            roleRepository.save(roleUser);
            System.out.println("ROLE_USER guardado con éxito.");

            // Crear el rol de administrador
            Role roleAdmin = new Role();
            roleAdmin.setName("ROLE_ADMIN");

            System.out.println("Guardando ROLE_ADMIN...");
            roleRepository.save(roleAdmin);
            System.out.println("ROLE_ADMIN guardado con éxito.");
        } else {
            System.out.println("Los roles ya existen en la base de datos.");
        }

        // Crear el usuario solo si no existen usuarios
        if (userRepository.count() == 0) {
            System.out.println("Creando el usuario 'Pilar_pato'...");
            // Crear el usuario
            User user = new User();
            user.setUsername("Pilar_pato");
            user.setPassword("1234");

            // Asignar roles a este usuario
            Role roleUser = roleRepository.findByName("ROLE_USER");
            Role roleAdmin = roleRepository.findByName("ROLE_ADMIN");

            if (roleUser != null && roleAdmin != null) {
                user.getRoles().add(roleUser);  // Asignar el rol de usuario
                user.getRoles().add(roleAdmin); // Asignar el rol de administrador
            } else {
                System.out.println("No se encontraron roles, verificando inserción.");
            }

            // Guardar el usuario con sus roles
            userRepository.save(user);
            System.out.println("Usuario 'Pilar_pato' creado con roles");
        } else {
            System.out.println("Ya existe al menos un usuario en la base de datos");
        }
    }
}
