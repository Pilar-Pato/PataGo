package dev.pilar.patago.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Utiliza BCrypt para cifrado de contraseñas
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Desactiva CSRF (para API REST)
            .authorizeHttpRequests(request -> 
                request.requestMatchers("/auth/register").permitAll() 
                       .requestMatchers("/api/admin/**").hasRole("ADMIN") // Solo acceso a ADMIN
                       .requestMatchers("/api/user/**").hasRole("USER") // Solo acceso a USER
                       .anyRequest().authenticated()) // Requiere autenticación para el resto
            .httpBasic(Customizer.withDefaults()); // Usa autenticación básica

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.inMemoryAuthentication()
            .withUser("root") // Usuario root con rol de ADMIN
            .password(passwordEncoder().encode("1234"))
            .roles("ADMIN")
            .and()
            .withUser("user") // Usuario user con rol de USER
            .password(passwordEncoder().encode("1234"))
            .roles("USER");

        return authenticationManagerBuilder.build();
    }
}
