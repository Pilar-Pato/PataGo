package dev.pilar.patago;

import dev.pilar.patago.jwt.AuthEntryPointJwt;
import dev.pilar.patago.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    private AuthTokenFilter authTokenFilter;

    private static final String[] WHITELIST = {
            "/auth/**"  // Rutas públicas (login, registro, etc.)
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Usamos Lambda DSL en Spring Security 6.x
        http
            .csrf(csrf -> csrf.disable()) // Desactivamos CSRF ya que no lo necesitamos con JWT
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers(WHITELIST).permitAll() // Rutas públicas para autenticación
                .anyRequest().authenticated() // Todas las demás rutas requieren autenticación
            )
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(unauthorizedHandler) // Manejo de excepciones de autenticación
            )
            .sessionManagement(sessionManagement -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Usamos JWT, no mantenemos sesión
            );

        // Agregar el filtro para verificar el JWT antes de cada solicitud
        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager(); // Devuelve el AuthenticationManager
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Utilizamos BCrypt para codificar las contraseñas
    }
}
