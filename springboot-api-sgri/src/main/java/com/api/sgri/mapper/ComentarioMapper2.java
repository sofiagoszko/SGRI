package com.api.sgri.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.api.sgri.dto.ComentarioDTO;
import com.api.sgri.exception.NotFoundException;
import com.api.sgri.model.ArchivoComentario;

import com.api.sgri.model.Comentario;
import com.api.sgri.service.ArchivoComentarioService;

import com.api.sgri.service.UsuarioEmpresaService;


@Component
public class ComentarioMapper2 {
    @Value("${app.base-url}")
    private String baseUrl;


    @Autowired
    private UsuarioEmpresaService usuarioEmpresaService;

    @Autowired
    private UsuarioEmpresaMapper usuarioEmpresaMapper;


    // Convertir de Comentario a ComentarioDTO
    @Mappings({
            @Mapping(target = "archivosComentario", expression = "java(mapArchivosComentario(comentario.getArchivosComentario()))")
    })
    public ComentarioDTO toDTO(Comentario comentario) {
        ComentarioDTO dto = new ComentarioDTO();

        dto.setId(comentario.getId());
        dto.setAsunto(comentario.getAsunto());
        dto.setDescripcion(comentario.getDescripcion());
        dto.setFecha_hora(comentario.getFecha_hora());
        dto.setUsuarioEmisorComentario(usuarioEmpresaMapper.toDTO(comentario.getUsuarioEmisorComentario()));
        dto.setRequerimiento(comentario.getRequerimiento().getId());
        // Mapear archivos adjuntos de Comentario
        if (comentario.getArchivosComentario() != null) {
            String url = baseUrl+"/api/requerimiento/archivoComentario/";
            List<String> archivosComentario = comentario.getArchivosComentario().stream()
                    .map(archivoComentario -> url + archivoComentario.getRuta())
                    .collect(Collectors.toList());
            dto.setArchivosComentario(archivosComentario);
        }

        return dto;
    }

    // Convertir de ComentarioDTO a Comentario
    @Mappings({
            @Mapping(target = "archivosComentario", expression = "java(mapArchivosComentarioDTO(dto.getArchivosComentario()))")
    })
    public Comentario fromDTO(ComentarioDTO dto) throws NotFoundException {
        Comentario comentario = new Comentario();

        comentario.setAsunto(dto.getAsunto());
        comentario.setDescripcion(dto.getDescripcion());
        comentario.setFecha_hora(dto.getFecha_hora());
        comentario.setUsuarioEmisorComentario(usuarioEmpresaMapper.fromDTO(dto.getUsuarioEmisorComentario()));
        // Mapear archivos adjuntos de ComentarioDTO a Comentario
        if (dto.getArchivosComentario() != null) {
            List<ArchivoComentario> archivosComentario = new ArrayList<>();
            for (String archivoRuta : dto.getArchivosComentario()) {
                ArchivoComentario archivoComentario = new ArchivoComentario();
                archivoComentario.setRuta(archivoRuta);
                archivosComentario.add(archivoComentario);
            }
            comentario.setArchivosComentario(archivosComentario);
        }

        return comentario;
    }

}
