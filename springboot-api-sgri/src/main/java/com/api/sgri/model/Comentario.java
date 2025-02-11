package com.api.sgri.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "comentarios")
public class Comentario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "asunto")
    private String asunto;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "fecha_hora")
    private String fecha_hora;

    //un comentario tiene una usuario emisor
    @ManyToOne
    @JoinColumn(name = "oid_usuario_emisor", nullable = false)
    @JsonIgnore
    private UsuarioEmpresa usuarioEmisorComentario;

    //una comentario esta asociado a un requerimiento
    @ManyToOne
    @JoinColumn(name = "oid_requerimiento", nullable = false)
    @JsonIgnore
    private Requerimiento requerimiento;
   
    @OneToMany(mappedBy = "comentario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArchivoComentario> archivosComentario = new ArrayList<>();
    
     


    public Comentario() {
        // Constructor vacío necesario para instanciar sin argumentos
    }

    public Comentario(String asunto, String descripcion, String fecha_hora, UsuarioEmpresa usuarioEmisorComentario, Requerimiento requerimiento ) {
        this.asunto = asunto;
        this.descripcion = descripcion;
        this.fecha_hora = fecha_hora;
        this.usuarioEmisorComentario = usuarioEmisorComentario;
        this.requerimiento = requerimiento;
        this.archivosComentario = new ArrayList<>();
    }


}
