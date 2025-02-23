package com.api.sgri.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import com.api.sgri.dto.ComentarioDTO2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.api.sgri.dto.ComentarioDTO;
import com.api.sgri.exception.NotFoundException;
import com.api.sgri.mapper.ComentarioMapper;
import com.api.sgri.model.ArchivoComentario;
import com.api.sgri.model.Comentario;
import com.api.sgri.model.Requerimiento;
import com.api.sgri.repository.ComentarioRepository;


@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private ArchivoComentarioService archivoComentarioService;

    @Autowired
    private ComentarioMapper comentarioMapper;

    public Comentario crearComentario(ComentarioDTO2 comentarioDTO, Requerimiento requerimiento, List<MultipartFile> archivos) throws Exception {
        try{
            Comentario comentario = comentarioMapper.fromDTO(comentarioDTO);
            comentario.setRequerimiento(requerimiento);

            if (archivos != null && !archivos.isEmpty() && archivos.size()<=5) {
                List<ArchivoComentario> archivosComentario = new ArrayList<>();
                for (MultipartFile archivo : archivos) {
                    String rutaArchivo = archivoComentarioService.guardarArchivo(archivo);
                    ArchivoComentario archivoComentario = new ArchivoComentario();
                    archivoComentario.setNombre(archivo.getOriginalFilename());
                    archivoComentario.setRuta(rutaArchivo);
                    archivoComentario.setComentario(comentario);
                    archivosComentario.add(archivoComentario);
                }
                comentario.setArchivosComentario(archivosComentario);
            }else if (archivos != null && archivos.size()>5) {
                throw new RuntimeException("No se pueden adjuntar más de 5 archivos.");
            }

            comentarioRepository.save(comentario);
            return comentario;
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public List<Comentario> getComentariosByRequerimiento(Long requerimientoID)throws NotFoundException {
        List<Comentario> comentarios = comentarioRepository.findByRequerimiento_Id(requerimientoID);
        if(comentarios.isEmpty()){
            throw new NotFoundException("No existen comentarios para el quererimiento ID: " + requerimientoID);
        }
        return comentarios;
    }
    public List<Comentario> getComentariosByUsuarioEmisor(Long usuarioEmisorID) throws NotFoundException {
        List<Comentario> comentarios = comentarioRepository.findByUsuarioEmisorComentario_Id(usuarioEmisorID);

        if(comentarios.isEmpty()){
            throw new NotFoundException("No existen comentarios realizados por el usuario: " + usuarioEmisorID);
        }
        return comentarios;
    }

    public Comentario getComentarioById(Long id) throws NotFoundException {
        Comentario comentario = comentarioRepository.findById(id).orElse(null);
        if(comentario == null){
            throw new NotFoundException("Comentario no encontrado con id " + id);
        }
        return comentario;
    }

     public List<ArchivoComentario> adjuntarArchivoComentario(Long comentarioId, List<MultipartFile> archivos) throws IOException {
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));

        if (archivos.size() > 5) {
            throw new RuntimeException("No se pueden adjuntar más de 5 archivos.");
        }

        if (comentario.getArchivosComentario() == null) {
            comentario.setArchivosComentario(new ArrayList<>());
        }

         List<ArchivoComentario> archivosCargados = new ArrayList<>();
         for (MultipartFile archivo : archivos) {
             String rutaArchivo = archivoComentarioService.guardarArchivo(archivo);
             ArchivoComentario archivoComentario = new ArchivoComentario();
             archivoComentario.setNombre(archivo.getOriginalFilename());
             archivoComentario.setRuta(rutaArchivo);
             archivoComentario.setComentario(comentario);

             archivosCargados.add(archivoComentario);
             comentario.getArchivosComentario().add(archivoComentario);
         }

        comentarioRepository.save(comentario);
        return archivosCargados;
    }

    public ArchivoComentario getArchivoComentarioById(Long comentarioId, Long archivoId) throws NotFoundException {
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));

        ArchivoComentario archivoComentario = archivoComentarioService.getArchivoComentarioById(archivoId);
        return archivoComentario;
    }

    public ArchivoComentario deleteArchivoComentarioById(Long comentarioId, Long archivoId) throws NotFoundException {
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));

        return archivoComentarioService.deleteArchivoComentarioById(archivoId);
    }

}
