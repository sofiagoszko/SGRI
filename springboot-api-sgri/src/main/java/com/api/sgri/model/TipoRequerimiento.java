package com.api.sgri.model;

import java.util.ArrayList;
import java.util.List;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "TiposRequerimiento")
public class TipoRequerimiento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo")
    private String codigo;
    @Column(name = "descripcion")
    private String descripcion;


    //un tipo de requerimiento puede tener asociados muchos requerimientos
    @OneToMany(mappedBy = "tipoRequerimiento", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Requerimiento> requerimientos;

    //un tipo de requerimiento puede tener asociados muchas categorias
    @OneToMany(mappedBy = "tipoRequerimiento", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CategoriaTipo> categorias;

    public TipoRequerimiento(){

    }

    public TipoRequerimiento(String codigo, String descripcion, List<Requerimiento> requerimientos,
            List<CategoriaTipo> categorias) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.requerimientos = new ArrayList<Requerimiento>();
        this.categorias = new ArrayList<CategoriaTipo>();
    }

    public void nuevoRequerimiento(Requerimiento requerimiento){
        requerimientos.add(requerimiento);
    }

    public void nuevaCategoria(CategoriaTipo categoria){
        categorias.add(categoria);
    }

}
