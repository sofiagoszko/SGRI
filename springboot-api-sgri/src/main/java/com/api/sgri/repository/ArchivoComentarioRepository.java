package com.api.sgri.repository;

import com.api.sgri.model.ArchivoComentario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArchivoComentarioRepository extends JpaRepository<ArchivoComentario, Long> {
}