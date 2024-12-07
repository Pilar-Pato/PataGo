package dev.pilar.patago;

import dev.pilar.patago.repository.RoleRepository;
import dev.pilar.patago.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @SuppressWarnings("unused")
    @Autowired
    private RoleRepository roleRepository;

    @SuppressWarnings("unused")
    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // Aquí no se está creando ni roles ni usuarios
        System.out.println("DataLoader ejecutado, pero sin crear roles ni usuarios");
    }
}
