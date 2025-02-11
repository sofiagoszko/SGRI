package com.api.sgri.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import com.api.sgri.repository.UsuarioEmpresaRepository;

@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;
    @Autowired
    private UsuarioEmpresaRepository usuarioEmpresaRepository;
    @Autowired
    private ArchivoComentarioService archivoComentarioService;

    @Autowired
    private ComentarioMapper comentarioMapper;

    public Comentario crearComentario(ComentarioDTO comentarioDTO, Requerimiento requerimiento) throws Exception {
        try{

            //validar que sean menos que 5 archivos adjuntos
//            List<Comentario> comentarios = comentarioRepository.findByRequerimiento_Id(comentarioDTO.getRequerimiento().getId());
//            if(comentarios.size()>=5){
//                throw new ListFullException("El comentario no puede tener más de 5 archivos adjuntos");
//            }

            Comentario comentario = comentarioMapper.fromDTO(comentarioDTO);
            comentario.setRequerimiento(requerimiento);

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

     public Comentario adjuntarArchivoComentario(Long comentarioId, List<MultipartFile> archivos) throws IOException {
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));

        if (archivos.size() > 5) {
            throw new RuntimeException("No se pueden adjuntar más de 5 archivos.");
        }

        if (comentario.getArchivosComentario() == null) {
            comentario.setArchivosComentario(new ArrayList<>());
        }

        for (MultipartFile archivo : archivos) {
            String rutaArchivo = archivoComentarioService.guardarArchivoComentario(archivo); 
            ArchivoComentario archivoComentario = new ArchivoComentario();
            archivoComentario.setNombre(archivo.getOriginalFilename());
            archivoComentario.setRuta(rutaArchivo);
            archivoComentario.setComentario(comentario);

            comentario.getArchivosComentario().add(archivoComentario); 
        }

        return comentarioRepository.save(comentario);  
    }


}
