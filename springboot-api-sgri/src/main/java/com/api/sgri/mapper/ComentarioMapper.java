package com.api.sgri.mapper;

import com.api.sgri.exception.NotFoundException;
import com.api.sgri.model.Requerimiento;
import com.api.sgri.repository.UsuarioEmpresaRepository;
import com.api.sgri.service.RequerimientoService;
import com.api.sgri.service.UsuarioEmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.api.sgri.dto.ComentarioDTO;
import com.api.sgri.model.Comentario;

@Component
public class ComentarioMapper {

    @Autowired
    private UsuarioEmpresaService usuarioEmpresaService;

    @Autowired
    private UsuarioEmpresaMapper usuarioEmpresaMapper;

    public ComentarioMapper(){

    }

    public ComentarioDTO toDTO(Comentario comentario){
        ComentarioDTO dto = new ComentarioDTO();

        dto.setId(comentario.getId());
        dto.setAsunto(comentario.getAsunto());
        dto.setDescripcion(comentario.getDescripcion());
        dto.setFecha_hora(comentario.getFecha_hora());
        dto.setUsuarioEmisorComentario(comentario.getUsuarioEmisorComentario().getId());
        //dto.setRequerimiento(comentario.getRequerimiento());
    
        return dto;

    }

    public Comentario fromDTO(ComentarioDTO dto) throws NotFoundException {
        Comentario comentario = new Comentario();

        comentario.setAsunto(dto.getAsunto());
        comentario.setDescripcion(dto.getDescripcion());
        comentario.setFecha_hora(dto.getFecha_hora());
        comentario.setUsuarioEmisorComentario(usuarioEmpresaMapper.fromDTO(usuarioEmpresaService.getUsuarioEmpresaById(dto.getUsuarioEmisorComentario())));
        comentario.setRequerimiento(dto.getRequerimiento());
        //comentario.setRequerimiento(requerimientoService.obtenerRequerimientoPorId(dto.getRequerimiento()));

        return comentario;

    }
}
