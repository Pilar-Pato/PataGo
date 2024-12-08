package dev.pilar.patago;

import dev.pilar.patago.model.User;
import dev.pilar.patago.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PatagoApplication implements CommandLineRunner {

    @Autowired
    private UserService userService;  // Inyecta el UserService

    public static void main(String[] args) {
        SpringApplication.run(PatagoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Crear un nuevo usuario
        User user = new User();
        user.setUsername("Pilar_pato prueba");
        user.setPassword("1234");
        user.setName("Pilar");
        user.setEmail("pilar@example.com");

        // Llamar al método createUser() para guardar el usuario
        User createdUser = userService.createUser(user);
        System.out.println("Usuario creado: " + createdUser.getUsername());
    }
}
