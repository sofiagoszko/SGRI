package com.api.sgri.repository;

import com.api.sgri.model.ArchivoComentario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArchivoComentarioRepository extends JpaRepository<ArchivoComentario, Long> {
    List<ArchivoComentario> findByComentario_Id(Long comentarioID);
}