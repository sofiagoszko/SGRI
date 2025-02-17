package com.api.sgri.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.api.sgri.exception.NotFoundException;
import com.api.sgri.exception.UnauthorizedException;
import com.api.sgri.jwt.JwtToken;
import com.api.sgri.model.UsuarioEmpresa;

@Service
public class AuthService {

    @Autowired
    private UsuarioEmpresaService usuarioEmpresaService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public String loginByCredentials(String userName, String password) throws NotFoundException, UnauthorizedException {
        
        UsuarioEmpresa usuarioEmpresa = usuarioEmpresaService.getUsuarioEmpresaByUserName(userName);
    
        if (usuarioEmpresa == null) {
            throw new NotFoundException("No existe usuario con nombre de usuario: " + userName);
        }
    
        // Validar la contraseña
        // if (!usuarioEmpresa.getPassword().equals(password)) {
        //     throw new UnauthorizedException("Credenciales inválidas");
        // }
        if (!passwordEncoder.matches(password, usuarioEmpresa.getPassword())) {
            throw new UnauthorizedException("Credenciales inválidas");
        }
    

        String token = JwtToken
                .generateToken()
                .addClaim("userName", usuarioEmpresa.getUserName())
                .addClaim("role", "USER")
                //.addClaim("password", usuarioEmpresa.getPassword())
                .setSubject(usuarioEmpresa.getUserName())
                .setTimeHours(10) //token valido por 10 horas
                .build();
    
        return token;
    }
}