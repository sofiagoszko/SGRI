package com.api.sgri.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.api.sgri.dto.ComentarioDTO;
import com.api.sgri.exception.NotFoundException;
import com.api.sgri.model.Comentario;
import com.api.sgri.model.TipoRequerimiento;
import com.api.sgri.model.UsuarioEmpresa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.api.sgri.dto.RequerimientoDTO;
import com.api.sgri.model.Requerimiento;
import java.util.ArrayList;

@Component
public class RequerimientoMapper {

    @Autowired
    private UsuarioEmpresaMapper usuarioMapper;

    @Autowired
    private ComentarioMapper comentarioMapper;


    @Autowired
    private CategoriaTipoMapper categoriaTipoMapper;


    public RequerimientoMapper() {
    }

    public RequerimientoDTO toDTO(Requerimiento requerimiento) {
        RequerimientoDTO dto = new RequerimientoDTO();

        dto.setId(requerimiento.getId());
        dto.setAsunto(requerimiento.getAsunto());
        dto.setCodigo(requerimiento.getCodigo());
        dto.setDescripcion(requerimiento.getDescripcion());
        dto.setEstado(requerimiento.getEstado());
        dto.setFechaHora(requerimiento.getFechaHora());
        dto.setPrioridad(requerimiento.getPrioridad());
        dto.setCategoriaTipo(requerimiento.getCategoria());

        // Mapear relaciones
        dto.setTipoRequerimiento(requerimiento.getTipoRequerimiento().getId());
        dto.setUsuarioEmisor(requerimiento.getUsuarioEmisor().getId());

        dto.setUsuarioDestinatario(requerimiento.getUsuarioDestinatario() != null ? requerimiento.getUsuarioDestinatario().getId() : null);

        if (requerimiento.getComentarios() != null) {
            dto.setComentarios(requerimiento.getComentarios().stream()
                .map(comentarioMapper::toDTO)
                .collect(Collectors.toList()));
        }else {
            requerimiento.setComentarios(new ArrayList<>());
        }

        return dto;
    }

    public Requerimiento fromDTO(RequerimientoDTO dto, TipoRequerimiento tipoRequerimiento, UsuarioEmpresa usuarioEmisor, UsuarioEmpresa usuarioDestinatario) throws NotFoundException {
        Requerimiento requerimiento = new Requerimiento();
    
        // Mapear atributos básicos
        requerimiento.setId(dto.getId());
        requerimiento.setAsunto(dto.getAsunto());
        requerimiento.setCodigo(dto.getCodigo());
        requerimiento.setDescripcion(dto.getDescripcion());
        requerimiento.setEstado(dto.getEstado());
        requerimiento.setFechaHora(dto.getFechaHora());
        requerimiento.setPrioridad(dto.getPrioridad());
        requerimiento.setCategoria(dto.getCategoriaTipo());
        requerimiento.setTipoRequerimiento(tipoRequerimiento);
        requerimiento.setUsuarioEmisor(usuarioEmisor);
        requerimiento.setUsuarioDestinatario(usuarioDestinatario);


        if (dto.getComentarios() != null) {
            List<Comentario> comentarios = new ArrayList<>();
            for (ComentarioDTO comentarioDTO : dto.getComentarios()) {
                try {
                    comentarios.add(comentarioMapper.fromDTO(comentarioDTO));
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
            }
            requerimiento.setComentarios(comentarios);
        } else {
            requerimiento.setComentarios(new ArrayList<>());
        }
        return requerimiento;
    }
   }
