package com.api.sgri.dto;

import com.api.sgri.model.TipoRequerimiento;

import lombok.Data;

@Data
public class CategoriaTipoDTO {
    
    private Long id;
    private String descripcion;
    //private TipoRequerimiento tipoRequerimiento;

    public CategoriaTipoDTO(){
        
    }

    public CategoriaTipoDTO(Long id, String descripcion, TipoRequerimiento tipoRequerimiento) {
        this.id = id;
        this.descripcion = descripcion;
        //this.tipoRequerimiento = tipoRequerimiento;
    }

    
}
