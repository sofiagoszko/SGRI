package com.api.sgri.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.api.sgri.exception.NotFoundException;
import com.api.sgri.model.ArchivoAdjunto;
import com.api.sgri.repository.ArchivoAdjuntoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ArchivoAdjuntoService {

    @Value("${ruta.archivos}")
    private String directorioArchivos;

    @Autowired
    private ArchivoAdjuntoRepository archivoAdjuntoRepository;


    public String guardarArchivo(MultipartFile archivo) throws IOException {
        String rutaArchivo = directorioArchivos + File.separator + archivo.getOriginalFilename();
        
        File destino = new File(rutaArchivo);

        destino.getParentFile().mkdirs();

        archivo.transferTo(destino);
    
        return destino.getAbsolutePath(); // Devolver la ruta completa
    }

    public List<ArchivoAdjunto> getArchivosAdjuntosByRequerimientoId (Long requerimientoID) throws NotFoundException {
        List<ArchivoAdjunto> archivosAdjuntos = archivoAdjuntoRepository.findByRequerimiento_Id(requerimientoID);

        if(archivosAdjuntos.isEmpty()){
            throw new NotFoundException("No existen archivos adjuntos pora el requerimiento: " + requerimientoID);
        }
        return archivosAdjuntos;
    }


}
