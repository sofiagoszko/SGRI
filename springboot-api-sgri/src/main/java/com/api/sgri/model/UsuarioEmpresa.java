package com.api.sgri.model;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
@Table(name = "UsuarioEmpresa")
public class UsuarioEmpresa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String nombre;
    @Column(name = "apellido")
    private String apellido;
    @Column(name = "email")
    private String email;

    @JsonIgnore
    @Column(name = "password")
    private String password;
    @Column(name = "nombre_usuario")
    private String userName;

    @JsonIgnore
    @Column(name = "legajo")
    private int legajo;
    
    @Column(name = "cargo")
    private String cargo;
    @Column(name = "departamento")
    private String departamento;
    @Column(name = "foto_perfil")
    private String fotoPerfil;

    //un usuario empresa puede tener muchos requerimientos emitidos
    @JsonIgnore
    @OneToMany(mappedBy = "usuarioEmisor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Requerimiento> requerimientosEmitidos;

    //un usuario empresa puede ser propietario de muchos requerimientos
    @JsonIgnore
    @OneToMany(mappedBy = "usuarioDestinatario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Requerimiento> requerimientosPropietario;

    //un usuario empresa puede emitir muchos comentarios
    @JsonIgnore
    @OneToMany(mappedBy = "usuarioEmisorComentario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comentario> comentarios;

    public UsuarioEmpresa(){}

    public UsuarioEmpresa(String nombre, String apellido, String email, String password, String userName, int legajo,
            String cargo, String departamento, List<Requerimiento> requerimientosEmitidos,
            List<Requerimiento> requerimientosPropietario, List<Comentario> comentarios) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.legajo = legajo;
        this.cargo = cargo;
        this.departamento = departamento;
        this.requerimientosEmitidos = new ArrayList<Requerimiento>();
        this.requerimientosPropietario = new ArrayList<Requerimiento>();
        this.comentarios = new ArrayList<Comentario>();
    };

    public UsuarioEmpresa(String nombre, String apellido, String email, String password, String userName,
        int legajo, String cargo, String departamento) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.legajo = legajo;
        this.cargo = cargo;
        this.departamento = departamento;
    }

    
//    public void nuevoRequerimientoEmitido(Requerimiento requerimiento){
//        requerimientosEmitidos.add(requerimiento);
//    }
//
//    public void nuevoRequerimientoPropietario(Requerimiento requerimiento){
//        requerimientosPropietario.add(requerimiento);
//    }
//    public void nuevoComentario(Comentario comentario){
//        comentarios.add(comentario);
//    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }
    
}
