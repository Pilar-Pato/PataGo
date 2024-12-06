package dev.pilar.patago.controller;

import dev.pilar.patago.jwt.JwtUtils;
import dev.pilar.patago.model.User;
import dev.pilar.patago.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthTokenController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    // Endpoint para autenticación (login)
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestParam String username, @RequestParam String password) {

        // Autenticación del usuario
        UsernamePasswordAuthenticationToken authenticationToken = 
            new UsernamePasswordAuthenticationToken(username, password);

        authenticationManager.authenticate(authenticationToken);

        // Si la autenticación es exitosa, se obtiene el usuario
        UserDetails userDetails = (UserDetails) authenticationToken.getPrincipal(); // Asumiendo que tienes este método en UserRepository

        // Generar el JWT
        String jwt = jwtUtils.generateTokenFromUsername(userDetails.getUsername());

        // Crear la respuesta
        Map<String, String> response = new HashMap<>();
        response.put("token", jwt);

        return ResponseEntity.ok(response);
    }

    // Endpoint para obtener el usuario actual (esto se usará para obtener datos del usuario autenticado)
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        // Obtener el nombre de usuario del contexto de seguridad
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username); // Asumiendo que tienes este método en UserRepository

        return ResponseEntity.ok(user);
    }
}
