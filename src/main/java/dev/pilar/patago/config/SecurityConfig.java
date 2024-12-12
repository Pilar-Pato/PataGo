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
        return new BCryptPasswordEncoder(); 
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) 
            .authorizeHttpRequests(request -> 
                request.requestMatchers("/auth/register").permitAll() 
                       .requestMatchers("/api/admin/**").hasRole("ADMIN") 
                       .requestMatchers("/api/user/**").hasRole("USER") 
                       .anyRequest().authenticated()) 
            .httpBasic(Customizer.withDefaults()); 

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.inMemoryAuthentication()
            .withUser("root") 
            .password(passwordEncoder().encode("1234"))
            .roles("ADMIN")
            .and()
            .withUser("user") 
            .password(passwordEncoder().encode("1234"))
            .roles("USER");

        return authenticationManagerBuilder.build();
    }
}
