package dev.pilar.patago;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PatagoApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(PatagoApplication.class, args);
    }

    @Override
    public void run(String... args) {
        
    }
}
