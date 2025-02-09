package com.api.sgri.dto;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class TipoRequerimientoDTO {

    private Long id;
    private String codigo;
    private String descripcion;
    //private List<RequerimientoDTO> requerimientos;
    private List<CategoriaTipoDTO> categorias = new ArrayList<>();

    public TipoRequerimientoDTO(){
        
    }

    public TipoRequerimientoDTO(Long id, String codigo, String descripcion, List<CategoriaTipoDTO> categorias) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        //this.requerimientos = requerimientos;
        this.categorias = (categorias != null) ? categorias : new ArrayList<>();
    }

    public void nuevaCategoria(CategoriaTipoDTO categoria){
        if (this.categorias == null) {
            this.categorias = new ArrayList<>();
        }
        this.categorias.add(categoria);
    }

}
