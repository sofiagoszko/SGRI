package com.api.sgri.service;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ArchivoComentarioService {

    // Leer la ruta desde el archivo de configuración
    @Value("${ruta.archivos}")
    private String directorioArchivos;

    public String guardarArchivoComentario(MultipartFile archivo) throws IOException {
        // Reemplazar los separadores de Windows con "/"
        String rutaArchivo = directorioArchivos + File.separator + archivo.getOriginalFilename();
        
        File destino = new File(rutaArchivo);
        
        // Asegurarse de que el directorio existe
        destino.getParentFile().mkdirs();
    
        // Guardar el archivo
        archivo.transferTo(destino);
    
        return destino.getAbsolutePath(); // Devolver la ruta completa
    }
    
}