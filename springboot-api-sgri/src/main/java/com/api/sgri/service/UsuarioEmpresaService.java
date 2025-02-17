package com.api.sgri.service;

import java.util.List;
import java.util.stream.Collectors;

import com.api.sgri.mapper.UsuarioEmpresaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.api.sgri.exception.DuplicateUserException;
import com.api.sgri.dto.UsuarioEmpresaDTO;
import com.api.sgri.exception.NotFoundException;
import com.api.sgri.model.UsuarioEmpresa;
import com.api.sgri.repository.UsuarioEmpresaRepository;


@Service
public class UsuarioEmpresaService {
    
    @Autowired
    private UsuarioEmpresaRepository usuarioEmpresaRepository;
    @Autowired
    private UsuarioEmpresaMapper usuarioEmpresaMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioEmpresa crearUsuarioEmpresa(UsuarioEmpresaDTO usuarioEmpresaDTO) throws Exception {
      
        Boolean existUsuarioEmpresaWithSameEmail = usuarioEmpresaRepository.existsByEmail(usuarioEmpresaDTO.getEmail());
        Boolean existUsuarioEmpresaWithSameUserName = usuarioEmpresaRepository.existsByUserName(usuarioEmpresaDTO.getUserName());
        Boolean existUsuarioEmpresaWithSameLegajo= usuarioEmpresaRepository.existsByLegajo(usuarioEmpresaDTO.getLegajo());

        if (existUsuarioEmpresaWithSameEmail || existUsuarioEmpresaWithSameUserName || existUsuarioEmpresaWithSameLegajo) {
            throw new DuplicateUserException("Nombre de usuario, email o legajo duplicado");
        }

        //UsuarioEmpresa usuarioEmpresa = usuarioEmpresaMapper.fromDTO(usuarioEmpresaDTO);

        String hashedPassword = passwordEncoder.encode(usuarioEmpresaDTO.getPassword());

            UsuarioEmpresa usuarioEmpresa = new UsuarioEmpresa(
            usuarioEmpresaDTO.getNombre(),
            usuarioEmpresaDTO.getApellido(),
            usuarioEmpresaDTO.getEmail(),
            //usuarioEmpresaDTO.getPassword(),
            hashedPassword,
            usuarioEmpresaDTO.getUserName(),
            usuarioEmpresaDTO.getLegajo(),
            usuarioEmpresaDTO.getCargo(),
            usuarioEmpresaDTO.getDepartamento()
        );

        usuarioEmpresaRepository.save(usuarioEmpresa);
        return usuarioEmpresa;
    }


    public List<UsuarioEmpresaDTO> getUsuarioEmpresas() {
        return usuarioEmpresaRepository.findAll().stream()
                .map(usuarioEmpresaMapper::toDTO)
                .collect(Collectors.toList());
    }


    public UsuarioEmpresaDTO getUsuarioEmpresaById(Long id) throws NotFoundException {
        UsuarioEmpresa usuario = usuarioEmpresaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        return usuarioEmpresaMapper.toDTO(usuario);
    }


    public UsuarioEmpresa getUsuarioEmpresaByEmail(String email) {
        return usuarioEmpresaRepository.findByEmail(email).orElse(null);
    }

    public UsuarioEmpresa getUsuarioEmpresaByUserName(String userName) {
        return usuarioEmpresaRepository.findByUserName(userName).orElse(null);
    }


    public UsuarioEmpresa deleteUsuarioEmpresa(Long id) throws NotFoundException {
            UsuarioEmpresa usuario = usuarioEmpresaRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("No existe usuario con id: " + id));

            usuarioEmpresaRepository.delete(usuario);
            
            return usuario;
    }

}
