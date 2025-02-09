package com.api.sgri.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.sgri.model.TipoRequerimiento;

@Repository
public interface TipoRequerimientoRepository extends JpaRepository<TipoRequerimiento, Long>  {
    Optional<TipoRequerimiento> findById(Long id);
}
