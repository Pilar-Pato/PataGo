package dev.pilar.patago;

import dev.pilar.patago.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "dev.pilar.patago.repository")  // Aquí se habilitan los repositorios JPA
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
