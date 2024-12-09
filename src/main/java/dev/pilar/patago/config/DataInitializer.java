package dev.pilar.patago.config;

import dev.pilar.patago.model.Dog;
import dev.pilar.patago.model.Reservation;
import dev.pilar.patago.model.Role;
import dev.pilar.patago.model.User;
import dev.pilar.patago.repository.DogRepository;
import dev.pilar.patago.repository.ReservationRepository;
import dev.pilar.patago.repository.RoleRepository;
import dev.pilar.patago.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DogRepository dogRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void run(String... args) throws Exception {
        try {
            // 1. Crear roles si no existen
            createRoles();

            // 2. Crear usuarios si no existen
            createUsers();

            // 3. Crear perros si no existen
            createDogs();  // Aquí llamamos a createDogs()

            // 4. Crear reservas para los perros si no existen
            createReservations();

        } catch (Exception e) {
            System.out.println("Error durante la inicialización de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createRoles() {
        try {
            // Crear rol 'USER' si no existe
            Role userRole = roleRepository.findByName("USER");
            if (userRole == null) {
                userRole = new Role("USER");
                roleRepository.save(userRole);
                System.out.println("Rol 'USER' creado.");
            }

            // Crear rol 'ADMIN' si no existe
            Role adminRole = roleRepository.findByName("ADMIN");
            if (adminRole == null) {
                adminRole = new Role("ADMIN");
                roleRepository.save(adminRole);
                System.out.println("Rol 'ADMIN' creado.");
            }
        } catch (Exception e) {
            System.out.println("Error al crear roles: " + e.getMessage());
        }
    }

    private void createUsers() {
        // Crear usuario 'root' con rol 'ADMIN'
        if (!userRepository.existsByUsername("root")) {
            User root = new User();
            root.setUsername("root");
            root.setPassword(passwordEncoder.encode("1234"));
            root.setName("Super Admin");
            root.setEmail("root@example.com");
            root.addRole(roleRepository.findByName("ADMIN"));  // Usar "ADMIN" en vez de "ROLE_ADMIN"
            userRepository.save(root);
        }

        // Crear usuario 'user' con rol 'USER'
        if (!userRepository.existsByUsername("user")) {
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("1234"));
            user.setName("User Example");
            user.setEmail("user@example.com");
            user.addRole(roleRepository.findByName("USER"));  // Usar "USER" en vez de "ROLE_USER"
            userRepository.save(user);
        }
    }

    private void createDogs() {
        try {
            // Crear perro 'Labrador' si no existe
            if (!dogRepository.existsByName("Labrador")) {
                User user = userRepository.findByUsername("user").orElse(null);
                if (user != null) {
                    Dog labrador = new Dog("Labrador", "Labrador", 1, "Mediano", "Dócil", user);
                    dogRepository.save(labrador);
                    System.out.println("Perro 'Labrador' creado para 'user'.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error al crear perros: " + e.getMessage());
        }
    }

    private void createReservations() {
        try {
            // Crear reserva para 'Labrador' de 'user'
            if (!reservationRepository.existsByDogNameAndUserUsername("Labrador", "user")) {
                Dog labrador = dogRepository.findByName("Labrador").orElse(null);
                User user = userRepository.findByUsername("user").orElse(null);
                if (labrador != null && user != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime startDate = LocalDateTime.parse("2024-12-01 10:00:00", formatter);
                    LocalDateTime endDate = LocalDateTime.parse("2024-12-01 18:00:00", formatter);
                    Reservation reservation = new Reservation(labrador, user, startDate, endDate, "CONFIRMADA");
                    reservationRepository.save(reservation);
                    System.out.println("Reserva creada para 'Labrador' de 'user'.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error al crear reservas: " + e.getMessage());
        }
    }
}
