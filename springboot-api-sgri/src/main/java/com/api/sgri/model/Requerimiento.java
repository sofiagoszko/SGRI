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

    //un requerimiento tiene un tipo asociado
    @ManyToOne
    @JoinColumn(name = "oid_tipo_requerimiento", nullable = false)
    @JsonIgnore
    private TipoRequerimiento tipoRequerimiento;
    //un requerimiento tiene una usuario emisor
    @ManyToOne
    @JoinColumn(name = "oid_usuario_emisor", nullable = false)
    private UsuarioEmpresa usuarioEmisor;

    //un requerimiento tiene una usuario destinatario
    @ManyToOne
    @JoinColumn(name = "oid_usuario_destinatario", nullable = true)
    private UsuarioEmpresa usuarioDestinatario;

    //un requerimiento puede tener asociados muchos comentarios
    @OneToMany(mappedBy = "requerimiento", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comentario> comentarios;

    //un requerimiento puede tener asociados varios archivos
    @OneToMany(mappedBy = "requerimiento", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ArchivoAdjunto> archivosAdjuntos = new ArrayList<>(); 

    public Requerimiento() {
        // Constructor vacío necesario para instanciar sin argumentos
    }

    public Requerimiento(String estado, String prioridad, String fechaHora, String asunto, String descripcion, String codigo, TipoRequerimiento tipoRequerimiento, UsuarioEmpresa usuarioEmisor, UsuarioEmpresa usuarioDestinatario, List<Comentario> comentarios) {
        this.estado = estado;
        this.prioridad = prioridad;
        this.fechaHora = fechaHora;
        this.asunto = asunto;
        this.descripcion = descripcion;
        this.codigo = codigo;
        this.tipoRequerimiento = tipoRequerimiento;
        this.usuarioEmisor = usuarioEmisor;
        this.usuarioDestinatario = usuarioDestinatario;
        this.comentarios = (comentarios != null) ? comentarios : new ArrayList<>();
    }

    public void nuevoComentario(Comentario comentario){
        comentarios.add(comentario);
    }


}