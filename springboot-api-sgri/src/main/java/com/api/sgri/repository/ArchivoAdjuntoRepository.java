package com.api.sgri.repository;

import com.api.sgri.model.ArchivoAdjunto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArchivoAdjuntoRepository extends JpaRepository<ArchivoAdjunto, Long> {
    List<ArchivoAdjunto> findByRequerimiento_Id(Long requerimientoID);

    List<ArchivoAdjunto> nombre(String nombre);

    ArchivoAdjunto findByRuta(String ruta);
}
