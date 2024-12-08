package dev.pilar.patago.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**")  // Permite todas las rutas
            .allowedOrigins("http://localhost:8080") // Permite solicitudes desde el origen especificado
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE") // Métodos permitidos
            .allowCredentials(true)  // Permite enviar cookies en las solicitudes
            .allowedHeaders("*"); // Permite todos los encabezados
    }
}