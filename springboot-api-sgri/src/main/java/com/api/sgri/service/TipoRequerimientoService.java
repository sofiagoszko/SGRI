package com.api.sgri.service;

import java.util.List;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.sgri.dto.TipoRequerimientoDTO;
import com.api.sgri.exception.NotFoundException;
import com.api.sgri.mapper.TipoRequerimientoMapper;
import com.api.sgri.model.CategoriaTipo;
import com.api.sgri.model.Requerimiento;
import com.api.sgri.model.TipoRequerimiento;
import com.api.sgri.model.UsuarioEmpresa;
import com.api.sgri.repository.CategoriaTipoRepository;
import com.api.sgri.repository.TipoRequerimientoRepository;

@Service
public class TipoRequerimientoService {

    @Autowired
    private TipoRequerimientoRepository tipoRequerimientoRepository;
    @Autowired
    private CategoriaTipoRepository categoriaTipoRepository;
    @Autowired
    private TipoRequerimientoMapper tipoRequerimientoMapper;

    public TipoRequerimiento crearTipoRequerimiento(TipoRequerimiento tipoRequerimiento){        
        tipoRequerimientoRepository.save(tipoRequerimiento);
        return tipoRequerimiento;
    }

    public List<TipoRequerimientoDTO> getTipoRequerimientos() {
        return tipoRequerimientoRepository.findAll().stream()
                .map(tipoRequerimientoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TipoRequerimiento getTipoRequerimentoById(Long id) throws NotFoundException {
        TipoRequerimiento tipoRequerimiento = tipoRequerimientoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Requerimiento no encontrado"));
        return tipoRequerimiento;
    }

    public TipoRequerimiento deleteTipoRequerimiento(Long id) throws NotFoundException {
        // Buscar al tipo por ID
        TipoRequerimiento tipoRequerimiento = tipoRequerimientoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No existe usuario con id: " + id));
        
        // Eliminar al usuario
        tipoRequerimientoRepository.delete(tipoRequerimiento);
        
        return tipoRequerimiento; // Retornar el tipo eliminado 
    }

    public CategoriaTipo crearCategoriaTipo(CategoriaTipo categoriaTipo){        
        categoriaTipoRepository.save(categoriaTipo);
        return categoriaTipo;
    }


    public List<CategoriaTipo> getCategorias(Long tipoRequerimientoId){
        return categoriaTipoRepository.findByTipoRequerimiento_Id(tipoRequerimientoId);

    }


}
