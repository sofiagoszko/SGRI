package com.api.sgri.mapper;

import com.api.sgri.dto.ArchivoComentarioDTO;
import com.api.sgri.model.ArchivoComentario;
import com.api.sgri.model.Comentario;
import com.api.sgri.repository.ComentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArchivoComentarioMapper {
    @Autowired
    ComentarioRepository comentarioRepository;

    public ArchivoComentarioDTO toDTO(ArchivoComentario archivoComentario) {
        ArchivoComentarioDTO dto = new ArchivoComentarioDTO();

        dto.setId(archivoComentario.getId());
        dto.setNombre(archivoComentario.getNombre());
        dto.setRuta(archivoComentario.getRuta());
        dto.setComentario(archivoComentario.getComentario().getId());

        return dto;
    }

    public ArchivoComentario fromDTO(ArchivoComentarioDTO archivoComentarioDTO) {
        ArchivoComentario archivoComentario = new ArchivoComentario();

        archivoComentario.setId(archivoComentarioDTO.getId());
        archivoComentario.setNombre(archivoComentarioDTO.getNombre());
        archivoComentario.setRuta(archivoComentarioDTO.getRuta());

        if (archivoComentarioDTO.getComentario() != null) {
            Comentario comentario = comentarioRepository.findById(archivoComentarioDTO.getComentario())
                    .orElseThrow(() -> new RuntimeException("Requerimiento no encontrado"));
            archivoComentario.setComentario(comentario);
        }

        return archivoComentario;
    }
}
