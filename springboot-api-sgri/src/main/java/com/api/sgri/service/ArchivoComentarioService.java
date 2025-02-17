package com.api.sgri.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.api.sgri.exception.NotFoundException;
import com.api.sgri.model.ArchivoAdjunto;
import com.api.sgri.model.ArchivoComentario;
import com.api.sgri.repository.ArchivoComentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ArchivoComentarioService {

    // Leer la ruta desde el archivo de configuración
    @Value("${ruta.archivos}")
    private String directorioArchivos;

    @Autowired
    ArchivoComentarioRepository archivoComentarioRepository;

    public String guardarArchivoComentario(MultipartFile archivo) throws IOException {
        // Reemplazar los separadores de Windows con "/"
        String rutaArchivo = directorioArchivos + File.separator + archivo.getOriginalFilename();
        
        File destino = new File(rutaArchivo);
        
        // Asegurarse de que el directorio existe
        destino.getParentFile().mkdirs();
    
        // Guardar el archivo
        archivo.transferTo(destino);
    
        return destino.getAbsolutePath();
    }

    public List<ArchivoComentario> getArchivosAdjuntosByComentarioId (Long comentarioID) throws NotFoundException {
        List<ArchivoComentario> archivosComentario = archivoComentarioRepository.findByComentario_Id(comentarioID);

        if(archivosComentario.isEmpty()){
            throw new NotFoundException("No existen archivos adjuntos pora el comentario: " + comentarioID);
        }
        return archivosComentario;
    }

    public ArchivoComentario getArchivoComentarioById(Long id) throws NotFoundException {
        ArchivoComentario archivoComentario = archivoComentarioRepository.findById(id).orElse(null);
        if(archivoComentario == null){
            throw new NotFoundException("Archivo no encontrado con id " + id);
        }
        return archivoComentario;
    }

    public ArchivoComentario deleteArchivoComentarioById(Long id) throws NotFoundException {
        ArchivoComentario archivoComentario = archivoComentarioRepository.findById(id).orElse(null);
        if(archivoComentario == null){
            throw new NotFoundException("Archivo no encontrado con id " + id);
        }
        archivoComentarioRepository.delete(archivoComentario);
        return archivoComentario;
    }
    
}