package com.api.sgri.mapper;

import com.api.sgri.dto.CategoriaTipoDTO;
import com.api.sgri.model.CategoriaTipo;
import org.springframework.stereotype.Component;

@Component
public class CategoriaTipoMapper {
    public CategoriaTipoMapper(){}

    public CategoriaTipoDTO toDTO(CategoriaTipo categoriaTipo) {
        CategoriaTipoDTO categoriaTipoDTO = new CategoriaTipoDTO();
        categoriaTipoDTO.setId(categoriaTipo.getId());
        categoriaTipoDTO.setDescripcion(categoriaTipo.getDescripcion());
        return categoriaTipoDTO;
    }
}
