package com.api.sgri.dto;

import com.api.sgri.model.Requerimiento;

import com.api.sgri.model.UsuarioEmpresa;
import lombok.Data;

@Data
public class ComentarioDTO {
    
    private Long id;
    private String asunto;
    private String descripcion;
    private String fecha_hora;
    private Long usuarioEmisorComentario;
    private Requerimiento requerimiento;

    public ComentarioDTO(){
        
    }

    public ComentarioDTO(String asunto, String descripcion, String fecha_hora,
            Long usuarioEmisorComentario, Requerimiento requerimiento) {
        this.asunto = asunto;
        this.descripcion = descripcion;
        this.fecha_hora = fecha_hora;
        this.usuarioEmisorComentario = usuarioEmisorComentario;
        this.requerimiento = requerimiento;
    }

    
}
