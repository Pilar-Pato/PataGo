package dev.pilar.patago.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@org.springframework.lang.NonNull HttpServletRequest request, 
                                    @org.springframework.lang.NonNull HttpServletResponse response, 
                                    @org.springframework.lang.NonNull FilterChain filterChain)
            throws ServletException, IOException {
        // Extraer JWT del encabezado
        String jwt = jwtUtils.getJwtFromHeader(request);
        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
            // Obtener nombre de usuario desde el token
            String username = jwtUtils.getUsernameFromJwtToken(jwt);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Crear el token de autenticación
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            // Establecer los detalles de la autenticación
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}
