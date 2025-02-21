package com.api.sgri.repository;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import com.api.sgri.model.Requerimiento;

public interface RequerimientoRepository extends JpaRepository<Requerimiento, Long> {
    Optional<Requerimiento> findById(Long requerimientoId);
}