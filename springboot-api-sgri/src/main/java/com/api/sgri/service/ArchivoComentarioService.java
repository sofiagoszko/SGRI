package com.api.sgri.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import com.api.sgri.exception.NotFoundException;
import com.api.sgri.model.ArchivoComentario;
import com.api.sgri.repository.ArchivoComentarioRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Data
@Service
public class ArchivoComentarioService {

    // Leer la ruta desde el archivo de configuración
    @Value("${ruta.comentarios}")
    private String directorioArchivos;

    @Autowired
    ArchivoComentarioRepository archivoComentarioRepository;

    public String guardarArchivo(MultipartFile archivo) throws IOException {
        String contentType = archivo.getContentType();
        if (!("application/pdf".equals(contentType)
                || "application/msword".equals(contentType)
                || "application/vnd.openxmlformats-officedocument.wordprocessingml.document".equals(contentType)
                || "application/vnd.ms-excel".equals(contentType)
                || "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(contentType))) {
            throw new IllegalArgumentException("Formato de archivo no permitido. Solo se permiten archivos Word, Excel o PDF.");
        }

        Path rutaDirectorio = Paths.get(directorioArchivos);
        if (!Files.exists(rutaDirectorio)) {
            Files.createDirectories(rutaDirectorio);
        }

        String nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();
        String rutaArchivo = directorioArchivos + File.separator + nombreArchivo;

        File destino = new File(rutaArchivo);
        destino.getParentFile().mkdirs();
        archivo.transferTo(destino);

        return nombreArchivo;
    }

    public List<ArchivoComentario> getArchivosAdjuntosByComentarioId (Long comentarioID) throws NotFoundException {
        List<ArchivoComentario> archivosComentario = archivoComentarioRepository.findByComentario_Id(comentarioID);
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
        if (archivoComentario == null) {
            throw new NotFoundException("Archivo no encontrado con id " + id);
        }

        File archivoFisico = new File(directorioArchivos + "/" + archivoComentario.getRuta());
        if (archivoFisico.exists()) {
            archivoFisico.delete();
        }

        archivoComentarioRepository.delete(archivoComentario);
        return archivoComentario;
    }

    public ArchivoComentario findByNombre(String nombreArchivo) throws NotFoundException {
        ArchivoComentario archivo = archivoComentarioRepository.findByNombre(nombreArchivo);
        if(archivo == null){
            throw new NotFoundException("Archivo no encontrado con el nombre " + nombreArchivo);
        }
        return archivo;
    }
    
}