package com.api.sgri.mapper;

import com.api.sgri.dto.ArchivoAdjuntoDTO;
import com.api.sgri.model.ArchivoAdjunto;
import com.api.sgri.model.Requerimiento;
import com.api.sgri.repository.RequerimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class ArchivoAdjuntoMapper {

    @Value("${app.base-url}")
    private String baseUrl;

    @Autowired
    RequerimientoRepository requerimientoRepository;

    public ArchivoAdjuntoDTO toDTO(ArchivoAdjunto archivoAdjunto) {
        ArchivoAdjuntoDTO dto = new ArchivoAdjuntoDTO();

        dto.setId(archivoAdjunto.getId());
        dto.setNombre(archivoAdjunto.getNombre());

        dto.setRequerimiento(archivoAdjunto.getRequerimiento().getId());

        String url = String.format("%s/api/requerimiento/%d/archivo/%s",
                baseUrl, archivoAdjunto.getRequerimiento().getId(), archivoAdjunto.getNombre());

        dto.setRuta(url);
        return dto;
    }

    public ArchivoAdjunto fromDTO(ArchivoAdjuntoDTO archivoAdjuntoDTO) {
        ArchivoAdjunto archivoAdjunto = new ArchivoAdjunto();

        archivoAdjunto.setId(archivoAdjuntoDTO.getId());
        archivoAdjunto.setNombre(archivoAdjuntoDTO.getNombre());
        archivoAdjunto.setRuta("uploads/" + archivoAdjuntoDTO.getNombre());

        if (archivoAdjuntoDTO.getRequerimiento() != null) {
            Requerimiento requerimiento = requerimientoRepository.findById(archivoAdjuntoDTO.getRequerimiento())
                    .orElseThrow(() -> new RuntimeException("Requerimiento no encontrado"));
            archivoAdjunto.setRequerimiento(requerimiento);
        }

        return archivoAdjunto;
    }

}
