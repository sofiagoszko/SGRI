package com.api.sgri.dto;

import lombok.Data;

@Data
public class UsuarioUpdateRequest {
    private UsuarioEmpresaDTO usuario;
    private String password;
}
