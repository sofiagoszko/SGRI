package com.api.sgri.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.api.sgri.dto.CategoriaTipoDTO;
import com.api.sgri.dto.TipoRequerimientoDTO;
import com.api.sgri.model.CategoriaTipo;
import com.api.sgri.model.TipoRequerimiento;

@Component
public class TipoRequerimientoMapper {
    @Autowired
    private CategoriaTipoMapper categoriaTipoMapper;

    public TipoRequerimientoMapper(){

    }

    public TipoRequerimientoDTO toDTO(TipoRequerimiento tipoRequerimiento){
        TipoRequerimientoDTO dto = new TipoRequerimientoDTO();

        dto.setId(tipoRequerimiento.getId());
        dto.setCodigo(tipoRequerimiento.getCodigo());
        dto.setDescripcion(tipoRequerimiento.getDescripcion());

        for(CategoriaTipo categoriaTipo: tipoRequerimiento.getCategorias()){
            CategoriaTipoDTO categoria = categoriaTipoMapper.toDTO(categoriaTipo);
            dto.nuevaCategoria(categoria);
        }
        return dto;
    }


    public TipoRequerimiento fromDTO(TipoRequerimientoDTO dto){
        TipoRequerimiento tipoRequerimiento = new TipoRequerimiento();

        tipoRequerimiento.setId(dto.getId());
        tipoRequerimiento.setCodigo(dto.getCodigo());
        tipoRequerimiento.setDescripcion(dto.getDescripcion());

        for(CategoriaTipoDTO categoriaTipoDTO: dto.getCategorias()){
            CategoriaTipo categoria = new CategoriaTipo();
            categoria.setId(categoriaTipoDTO.getId());
            categoria.setDescripcion(categoriaTipoDTO.getDescripcion());
            tipoRequerimiento.nuevaCategoria(categoria);
        }
        return tipoRequerimiento;
    }
}
