package dev.pilar.patago;

import dev.pilar.patago.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PatagoApplication implements CommandLineRunner {

    @Autowired
    private UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(PatagoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Crear un usuario si no existe
        userService.createUser();
    }
}
