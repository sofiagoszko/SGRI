package com.api.sgri.dto;

import lombok.Data;

import java.util.List;

@Data
public class RequerimientoDTO2 {

    private Long id;
    private String estado;
    private String prioridad;
    private String fechaHora;
    private String asunto;
    private String descripcion;
    private String codigo;
    private String categoriaTipo;
    private TipoRequerimientoDTO tipoRequerimiento;
    private UsuarioEmpresaDTO usuarioEmisor;
    private UsuarioEmpresaDTO usuarioDestinatario;
    private List<ComentarioDTO> comentarios;
    private List<String> archivosAdjuntos;

    public RequerimientoDTO2(){

    }

    public RequerimientoDTO2(Long id, String estado, String prioridad, String fechaHora,
                             String asunto, String descripcion, String codigo, String categoriaTipo,
                             TipoRequerimientoDTO tipoRequerimiento, UsuarioEmpresaDTO usuarioEmisor,
                             UsuarioEmpresaDTO usuarioDestinatario, List<ComentarioDTO> comentarios,
                             List<String> archivosAdjuntos) {
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
        this.comentarios = comentarios;
        this.archivosAdjuntos = archivosAdjuntos;
    }
}
