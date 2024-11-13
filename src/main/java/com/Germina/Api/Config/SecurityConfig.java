package com.Germina.Api.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable()) // Desactiva CSRF para API REST
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Configura CORS
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(publicEndpoints()).permitAll() // Define endpoints públicos
                        .anyRequest().authenticated()) // Requiere autenticación para todas las demás rutas
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Sin manejo de sesiones
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // Añade el filtro JWT

        return httpSecurity.build();
    }

    // Configuración de CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("https://navvi.netlify.app/")); // Define el origen de tu frontend (ajústalo si es necesario)
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*")); // Permite todos los headers
        configuration.setAllowCredentials(true); // Permite el uso de credenciales
        configuration.setExposedHeaders(List.of("Authorization", "Content-Type", "Accept")); // Expone headers importantes

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplica la configuración CORS a todas las rutas
        return source;
    }

    // Rutas públicas que no requieren autenticación
    private RequestMatcher publicEndpoints() {
        return new OrRequestMatcher(
                new AntPathRequestMatcher("/api/auth/**"),
                new AntPathRequestMatcher("/api/TempUser/**"),
                new AntPathRequestMatcher("/api/TempUser/saveTempUser"),
                new AntPathRequestMatcher("/activate/**"),
                new AntPathRequestMatcher("/api/auth/activate-email") // Ruta específica de activación
        );
    }
}
