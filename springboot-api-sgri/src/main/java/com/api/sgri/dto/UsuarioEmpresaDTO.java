package com.api.sgri.dto;

import com.api.sgri.model.UsuarioEmpresa;

import lombok.Data;

@Data
public class UsuarioEmpresaDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String userName;
    private int legajo;
    private String cargo;
    private String departamento;

    public UsuarioEmpresaDTO(){
        
    }

    public UsuarioEmpresaDTO(Long id, String nombre, String apellido, String email, String password, String userName,
                            int legajo, String cargo, String departamento) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.legajo = legajo;
        this.cargo = cargo;
        this.departamento = departamento;
    }



}
