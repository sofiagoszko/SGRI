package com.api.sgri.dto;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class RequerimientoDTO {
    
    private Long id;
    private String estado;
    private String prioridad;
    private String fechaHora;
    private String asunto;
    private String descripcion;
    private String codigo;
    private String categoriaTipo;
    private Long tipoRequerimiento;
    private Long usuarioEmisor;
    private Long usuarioDestinatario;
    private List<ComentarioDTO> comentarios;
    private List<String> archivosAdjuntos;

    public RequerimientoDTO(){

    }

    public RequerimientoDTO(Long id, String estado, String prioridad, String fechaHora, String asunto, String descripcion,
            String codigo, String categoriaTipo, Long tipoRequerimiento,
            Long usuarioEmisor, Long usuarioDestinatario) {
        this.id = id;
        this.estado = estado;
        this.prioridad = prioridad;
        this.fechaHora = fechaHora;
        this.asunto = asunto;
        this.descripcion = descripcion;
        this.codigo = codigo;
        this.categoriaTipo = categoriaTipo;
        this.tipoRequerimiento = tipoRequerimiento;
        this.usuarioEmisor = usuarioEmisor;
        this.usuarioDestinatario = usuarioDestinatario;
        this.comentarios = new ArrayList<>();
    }

}
