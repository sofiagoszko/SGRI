package com.api.sgri.model;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "Requerimientos")
public class Requerimiento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "estado")
    private String estado;
    @Column(name = "prioridad")
    private String prioridad;
    @Column(name = "fecha_hora")
    private String fechaHora;
    @Column(name = "asunto")
    private String asunto;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "codigo")
    private String codigo;
    @Column(name = "categoria")
    private String categoria;


    @ManyToOne
    @JoinColumn(name = "oid_tipo_requerimiento", nullable = false)
    @JsonIgnore
    private TipoRequerimiento tipoRequerimiento;

    @ManyToOne
    @JoinColumn(name = "oid_usuario_emisor", nullable = false)
    private UsuarioEmpresa usuarioEmisor;


    @ManyToOne
    @JoinColumn(name = "oid_usuario_destinatario", nullable = true)
    private UsuarioEmpresa usuarioDestinatario;


    @OneToMany(mappedBy = "requerimiento", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comentario> comentarios;


    @OneToMany(mappedBy = "requerimiento", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ArchivoAdjunto> archivosAdjuntos = new ArrayList<>(); 

    public Requerimiento() {
    }

    public Requerimiento(String estado, String prioridad, String fechaHora, String asunto, String descripcion, String codigo, TipoRequerimiento tipoRequerimiento, UsuarioEmpresa usuarioEmisor, UsuarioEmpresa usuarioDestinatario, List<ArchivoAdjunto> archivosAdjuntos, List<Comentario> comentarios) {
        this.estado = estado;
        this.prioridad = prioridad;
        this.fechaHora = fechaHora;
        this.asunto = asunto;
        this.descripcion = descripcion;
        this.codigo = codigo;
        this.tipoRequerimiento = tipoRequerimiento;
        this.usuarioEmisor = usuarioEmisor;
        this.usuarioDestinatario = usuarioDestinatario;
        this.archivosAdjuntos = (archivosAdjuntos != null) ? archivosAdjuntos : new ArrayList<>();
        this.comentarios = (comentarios != null) ? comentarios : new ArrayList<>();
    }

    public void nuevoComentario(Comentario comentario){
        comentarios.add(comentario);
    }


}