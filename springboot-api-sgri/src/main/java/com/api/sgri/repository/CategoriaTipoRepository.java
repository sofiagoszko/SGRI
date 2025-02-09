package com.api.sgri.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.sgri.model.CategoriaTipo;

public interface CategoriaTipoRepository extends JpaRepository<CategoriaTipo, Long> {
    List<CategoriaTipo> findByTipoRequerimiento_Id(Long tipoRequerimientoId);

    boolean existsByIdAndTipoRequerimiento_Id(Long categoriaId, Long tipoRequerimientoId);
}