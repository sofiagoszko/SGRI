package com.api.sgri.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.api.sgri.jwt.JwtAuthFilter;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Desactiva CSRF, se recomienda si estás usando autenticación por token
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/usuario-empresa/", "/api/usuario-empresa/registracion",
                                "/api/usuario-empresa/credenciales").permitAll()  // Rutas públicas
                        .anyRequest().hasRole("USER")  // Solo usuarios con ROLE_USER pueden acceder

                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);  // Filtro para validar el JWT

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Configura el codificador de contraseñas
    }
}
