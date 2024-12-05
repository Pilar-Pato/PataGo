package dev.pilar.patago.jwt;

import dev.pilar.patago.model.User;
import dev.pilar.patago.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca al usuario en la base de datos por su nombre de usuario
        User user = userRepository.findByUsername(username);
        
        // Si no se encuentra el usuario, lanzamos una excepción
        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }

        // Devolvemos un CustomUserDetails que envuelve el User
        return new CustomUserDetails(user);  // Usamos CustomUserDetails para envolver el objeto User
    }
}