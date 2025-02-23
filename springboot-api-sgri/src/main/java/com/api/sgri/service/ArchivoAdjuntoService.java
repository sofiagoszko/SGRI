package com.api.sgri.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.api.sgri.exception.NotFoundException;
import com.api.sgri.model.ArchivoAdjunto;
import com.api.sgri.repository.ArchivoAdjuntoRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Data
@Service
public class ArchivoAdjuntoService {

    @Value("${ruta.archivos}")
    private String directorioArchivos;

    @Autowired
    private ArchivoAdjuntoRepository archivoAdjuntoRepository;


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

    public List<ArchivoAdjunto> getArchivosAdjuntosByRequerimientoId (Long requerimientoID) throws NotFoundException {
        List<ArchivoAdjunto> archivosAdjuntos = archivoAdjuntoRepository.findByRequerimiento_Id(requerimientoID);

        if(archivosAdjuntos.isEmpty()){
            throw new NotFoundException("No existen archivos adjuntos pora el requerimiento: " + requerimientoID);
        }
        return archivosAdjuntos;
    }

    public ArchivoAdjunto getArchivoAdjuntoById(Long id) throws NotFoundException {
        ArchivoAdjunto archivoAdjunto = archivoAdjuntoRepository.findById(id).orElse(null);
        if(archivoAdjunto == null){
            throw new NotFoundException("Archivo no encontrado con id " + id);
        }
        return archivoAdjunto;
    }

//    public ArchivoAdjunto deleteArchivoAdjuntoById(Long id) throws NotFoundException {
//        ArchivoAdjunto archivoAdjunto = archivoAdjuntoRepository.findById(id).orElse(null);
//        if(archivoAdjunto == null){
//            throw new NotFoundException("Archivo no encontrado con id " + id);
//        }
//        archivoAdjuntoRepository.delete(archivoAdjunto);
//        return archivoAdjunto;
//    }

    public ArchivoAdjunto deleteArchivoAdjuntoById(Long id) throws NotFoundException {
        ArchivoAdjunto archivoAdjunto = archivoAdjuntoRepository.findById(id).orElse(null);
        if (archivoAdjunto == null) {
            throw new NotFoundException("Archivo no encontrado con id " + id);
        }

        File archivoFisico = new File(directorioArchivos + "/" + archivoAdjunto.getRuta());
        if (archivoFisico.exists()) {
            archivoFisico.delete();
        }

        archivoAdjuntoRepository.delete(archivoAdjunto);
        return archivoAdjunto;
    }

    public ArchivoAdjunto findByNombre(String nombreArchivo) throws NotFoundException {
        ArchivoAdjunto archivo = archivoAdjuntoRepository.findByNombre(nombreArchivo);
        if(archivo == null){
            throw new NotFoundException("Archivo no encontrado con el nombre " + nombreArchivo);
        }
        return archivo;
    }

}
