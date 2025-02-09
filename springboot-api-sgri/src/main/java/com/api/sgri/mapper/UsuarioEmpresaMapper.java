package com.api.sgri.mapper;

import com.api.sgri.model.UsuarioEmpresa;
import com.api.sgri.dto.UsuarioEmpresaDTO;

import org.springframework.stereotype.Component;

@Component
public class UsuarioEmpresaMapper {

    public UsuarioEmpresaMapper() {
    }

    public UsuarioEmpresaDTO toDTO(UsuarioEmpresa usuarioEmpresa) {
        UsuarioEmpresaDTO dto = new UsuarioEmpresaDTO();

        dto.setId(usuarioEmpresa.getId());
        dto.setNombre(usuarioEmpresa.getNombre());
        dto.setApellido(usuarioEmpresa.getApellido());
        dto.setEmail(usuarioEmpresa.getEmail());
        dto.setPassword(usuarioEmpresa.getPassword());
        dto.setUserName(usuarioEmpresa.getUserName());
        dto.setLegajo(usuarioEmpresa.getLegajo());
        dto.setCargo(usuarioEmpresa.getCargo());
        dto.setDepartamento(usuarioEmpresa.getDepartamento());

        return dto;
    }

    public UsuarioEmpresa fromDTO(UsuarioEmpresaDTO dto) {
        UsuarioEmpresa usuarioEmpresa = new UsuarioEmpresa();

        usuarioEmpresa.setId(dto.getId());
        usuarioEmpresa.setNombre(dto.getNombre());
        usuarioEmpresa.setApellido(dto.getApellido());
        usuarioEmpresa.setEmail(dto.getEmail());
        usuarioEmpresa.setPassword(dto.getPassword());
        usuarioEmpresa.setUserName(dto.getUserName());
        usuarioEmpresa.setLegajo(dto.getLegajo());
        usuarioEmpresa.setCargo(dto.getCargo());
        usuarioEmpresa.setDepartamento(dto.getDepartamento());

        return usuarioEmpresa;
    }
}
