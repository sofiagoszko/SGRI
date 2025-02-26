package com.api.sgri.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

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

    @ManyToOne
    @JoinColumn(name = "oid_tipo_requerimiento", nullable = false)
    @JsonIgnore
    private TipoRequerimiento tipoRequerimiento;




    public CategoriaTipo(){

    }

    public CategoriaTipo(String descripcion, TipoRequerimiento tipoRequerimiento) {
        this.descripcion = descripcion;
        this.tipoRequerimiento = tipoRequerimiento;
  
    }

   

}
