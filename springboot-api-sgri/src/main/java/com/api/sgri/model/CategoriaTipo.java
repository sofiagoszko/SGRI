package com.api.sgri.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

@JsonIgnoreProperties("tipoRequerimiento")
@Entity
@Data
@Table(name = "CategoriasTipo")
public class CategoriaTipo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "descripcion")
    private String descripcion;

    //una categoria tiene un tipo asociado
    @ManyToOne
    @JoinColumn(name = "oid_tipo_requerimiento", nullable = false)
    @JsonIgnore
    private TipoRequerimiento tipoRequerimiento;

    //una categoria puede tener asociados muchos requerimientos
//    @OneToMany(mappedBy = "categoriaTipo", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Requerimiento> requerimientos;



    public CategoriaTipo(){

    }

    public CategoriaTipo(String descripcion, TipoRequerimiento tipoRequerimiento) {
        this.descripcion = descripcion;
        this.tipoRequerimiento = tipoRequerimiento;
  
    }

   

}
