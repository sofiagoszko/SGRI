package com.api.sgri.jwt;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        logger.info("Iniciando validación de token...");
        
        String header = request.getHeader(HEADER);
        if (header != null && header.startsWith(PREFIX)) {
            String token = header.substring(PREFIX.length());
            logger.info("Token recibido: " + token);
            
            try {
                Claims claims = JwtToken.getPayload(token);
                if (claims != null) {
                    String userName = claims.getSubject();
                    String role = claims.get("role", String.class);
                    logger.info("Token válido para el usuario: " + userName);


                    List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));


                    Authentication authentication = new UsernamePasswordAuthenticationToken(userName, null, authorities);


                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    request.setAttribute("userName", userName);
                    logger.info("Usuario autenticado: " + userName);
                    
                } else {
                    logger.warn("Token inválido o expirado");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Token inválido o expirado");
                    return;
                }
            } catch (JwtException e) {
                logger.error("Error procesando el token: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Error procesando el token");
                return;
            }
        } else {
            logger.warn("Cabecera Authorization no presente o sin prefijo Bearer");
        }

        filterChain.doFilter(request, response);
    }
}

