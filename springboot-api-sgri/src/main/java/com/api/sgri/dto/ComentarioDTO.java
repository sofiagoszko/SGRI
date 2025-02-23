package com.api.sgri.dto;
import java.util.List;



import lombok.Data;

@Data
public class ComentarioDTO {
    
    private Long id;
    private String asunto;
    private String descripcion;
    private String fecha_hora;
    private UsuarioEmpresaDTO usuarioEmisorComentario;
    private Long requerimiento;
    private List<String> archivosComentario;

    public ComentarioDTO(){
        
    }

    public ComentarioDTO(String asunto, String descripcion, String fecha_hora,
                         UsuarioEmpresaDTO usuarioEmisorComentario, Long requerimiento) {
        this.asunto = asunto;
        this.descripcion = descripcion;
        this.fecha_hora = fecha_hora;
        this.usuarioEmisorComentario = usuarioEmisorComentario;
        this.requerimiento = requerimiento;
    }

    
}
