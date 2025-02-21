package com.api.sgri.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.api.sgri.dto.UsuarioEmpresaDTO;
import com.api.sgri.exception.DuplicateUserException;
import com.api.sgri.exception.NotFoundException;
import com.api.sgri.mapper.UsuarioEmpresaMapper;
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

    @Value("${ruta.archivos}")
    private String rutaArchivos;

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

    public UsuarioEmpresa getUsuarioEmpresaByIdEntity(Long id) throws NotFoundException {
        return usuarioEmpresaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
    }

    public UsuarioEmpresaDTO updateUsuarioEmpresa(UsuarioEmpresa usuarioEmpresa, UsuarioEmpresaDTO usuarioEmpresaDTO) throws Exception {
        Boolean existUsuarioEmpresaWithSameEmail = usuarioEmpresaRepository.existsByEmail(usuarioEmpresaDTO.getEmail())
                && !usuarioEmpresa.getEmail().equals(usuarioEmpresaDTO.getEmail());
        Boolean existUsuarioEmpresaWithSameUserName = usuarioEmpresaRepository.existsByUserName(usuarioEmpresaDTO.getUserName())
                && !usuarioEmpresa.getUserName().equals(usuarioEmpresaDTO.getUserName());
        Boolean existUsuarioEmpresaWithSameLegajo = usuarioEmpresaRepository.existsByLegajo(usuarioEmpresaDTO.getLegajo())
                && usuarioEmpresa.getLegajo() != usuarioEmpresaDTO.getLegajo();

        if (existUsuarioEmpresaWithSameEmail || existUsuarioEmpresaWithSameUserName || existUsuarioEmpresaWithSameLegajo) {
            throw new DuplicateUserException("Nombre de usuario, email o legajo duplicado");
        }
        usuarioEmpresa.setNombre(usuarioEmpresaDTO.getNombre());
        usuarioEmpresa.setApellido(usuarioEmpresaDTO.getApellido());
        usuarioEmpresa.setEmail(usuarioEmpresaDTO.getEmail());
        usuarioEmpresa.setUserName(usuarioEmpresaDTO.getUserName());
        usuarioEmpresa.setLegajo(usuarioEmpresaDTO.getLegajo());
        usuarioEmpresa.setCargo(usuarioEmpresaDTO.getCargo());
        usuarioEmpresa.setDepartamento(usuarioEmpresaDTO.getDepartamento());

        if (usuarioEmpresaDTO.getPassword() != null && !usuarioEmpresaDTO.getPassword().isEmpty()) {
            usuarioEmpresa.setPassword(passwordEncoder.encode(usuarioEmpresaDTO.getPassword()));
        }

        usuarioEmpresaRepository.save(usuarioEmpresa);
        return usuarioEmpresaMapper.toDTO(usuarioEmpresa);
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

    public UsuarioEmpresa getUsuarioEmpresaByEmail(String email) throws NotFoundException {
        UsuarioEmpresa usuario = usuarioEmpresaRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        return usuario;
    }
//    public UsuarioEmpresa getUsuarioEmpresaByEmail(String email) {
//        return usuarioEmpresaRepository.findByEmail(email).orElse(null);
//    }

    public UsuarioEmpresa getUsuarioEmpresaByUserName(String userName) {
        return usuarioEmpresaRepository.findByUserName(userName).orElse(null);
    }


    public UsuarioEmpresa deleteUsuarioEmpresa(Long id) throws NotFoundException {
            UsuarioEmpresa usuario = usuarioEmpresaRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("No existe usuario con id: " + id));

            usuarioEmpresaRepository.delete(usuario);
            
            return usuario;
    }

    public UsuarioEmpresa actualizarFotoPerfil(Long id, MultipartFile archivo) throws IOException, NotFoundException {
        UsuarioEmpresa usuario = usuarioEmpresaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
    
        if (archivo == null || archivo.isEmpty()) {
            throw new IOException("No se proporcionó un archivo válido");
        }
    
        String nombreArchivo = UUID.randomUUID().toString() + "-" + archivo.getOriginalFilename();
        Path destino = Paths.get(rutaArchivos, nombreArchivo);
        Files.copy(archivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);
    
        usuario.setFotoPerfil(nombreArchivo);
        return usuarioEmpresaRepository.save(usuario);
    }
    
}
