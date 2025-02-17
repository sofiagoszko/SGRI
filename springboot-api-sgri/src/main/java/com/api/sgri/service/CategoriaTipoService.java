package com.api.sgri.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.sgri.model.CategoriaTipo;
import com.api.sgri.model.TipoRequerimiento;
import com.api.sgri.repository.CategoriaTipoRepository;
import com.api.sgri.repository.TipoRequerimientoRepository;
@Service
public class CategoriaTipoService {

    @Autowired
    private CategoriaTipoRepository categoriaTipoRepository;

    @Autowired
    private TipoRequerimientoRepository tipoRequerimientoRepository;


    public List<CategoriaTipo> obtenerCategorias() {
        return categoriaTipoRepository.findAll();
    }


    public List<CategoriaTipo> obtenerCategoriasPorTipo(Long tipoId) {
        return categoriaTipoRepository.findAll()
                .stream()
                .filter(categoria -> categoria.getTipoRequerimiento().getId().equals(tipoId))
                .toList();
    }


    public CategoriaTipo crearCategoria(String descripcion, Long tipoRequerimientoId) {
        Optional<TipoRequerimiento> tipoRequerimiento = tipoRequerimientoRepository.findById(tipoRequerimientoId);

        if (tipoRequerimiento.isPresent()) {
            CategoriaTipo categoria = new CategoriaTipo(descripcion, tipoRequerimiento.get());
            return categoriaTipoRepository.save(categoria);
        } else {
            throw new RuntimeException("Tipo de requerimiento no encontrado");
        }
    }
}
