package com.api.sgri.controller;
import java.util.ArrayList;
import java.util.List;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;


import com.api.sgri.exception.NotFoundException;
import com.api.sgri.mapper.ArchivoAdjuntoMapper;
import com.api.sgri.service.ArchivoAdjuntoService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.api.sgri.dto.ArchivoAdjuntoDTO;
import com.api.sgri.dto.RequerimientoDTO;
import com.api.sgri.mapper.RequerimientoMapper;
import com.api.sgri.model.ArchivoAdjunto;
import com.api.sgri.model.Requerimiento;
import com.api.sgri.response.HttpBodyResponse;
import com.api.sgri.response.ResponseFactory;
import com.api.sgri.service.RequerimientoService;


@RestController
@RequestMapping("api/requerimiento")
@CrossOrigin(origins = "*")
public class RequerimientoController {

    @Autowired
    private RequerimientoService requerimientoService;

    @Autowired
    private ResponseFactory responseFactory;

    @Autowired
    private RequerimientoMapper requerimientoMapper;
    @Autowired
    private ArchivoAdjuntoService archivoAdjuntoService;
    @Autowired
    private ArchivoAdjuntoMapper archivoAdjuntoMapper;


    @GetMapping("/")
    public ResponseEntity<Object> publicRoute() {
        return ResponseEntity.ok("Ruta publica...");
    }


    @GetMapping("/requerimientos")
    public ResponseEntity<Object> getRequerimientos(@RequestParam(required = false) String param) {
        try {

            List<RequerimientoDTO> requerimientos = requerimientoService.obtenerRequerimientos();

            HttpBodyResponse data = new HttpBodyResponse.Builder()
                    .message("Requerimientos obtenidos con éxito")
                    .status("Success")
                    .statusCode(200)
                    .data(requerimientos)
                    .build();
            return ResponseEntity.status(data.getStatusCode()).body(data);
        } catch (Exception e) {
            return responseFactory.internalServerError();
        }
    }

    @GetMapping("/requerimientos/{id}")
    public ResponseEntity<Object> getRequerimiento(@PathVariable Long id) {
        try {

            Requerimiento requerimiento = requerimientoService.obtenerRequerimientoPorId(id);
            RequerimientoDTO requerimientoDTO = requerimientoMapper.toDTO(requerimiento);

            HttpBodyResponse data = new HttpBodyResponse.Builder()
                    .message("Requerimiento obtenido con éxito")
                    .status("Success")
                    .statusCode(200)
                    .data(requerimientoDTO)
                    .build();
            return ResponseEntity.status(data.getStatusCode()).body(data);

        } catch (Exception e) {
            return responseFactory.internalServerError();
        }
    }

    @PutMapping("/requerimientos/{id}")
    public ResponseEntity<Object> updateRequerimiento(@PathVariable Long id, @RequestBody RequerimientoDTO requerimientoDTO) {
        try {
            Requerimiento requerimiento = requerimientoService.obtenerRequerimientoPorId(id);


            RequerimientoDTO requerimientoActualizado = requerimientoService.updateRequerimiento(requerimiento, requerimientoDTO);

            HttpBodyResponse data = new HttpBodyResponse.Builder()
                    .message("Requerimiento actualizado con éxito")
                    .status("Success")
                    .statusCode(200)
                    .data(requerimientoActualizado)
                    .build();
            return ResponseEntity.status(data.getStatusCode()).body(data);

        } catch (NotFoundException e) {
            return responseFactory.errorNotFound("No existe requerimiento con id: " + id);
        }catch (Exception e) {
            return responseFactory.internalServerError();
        }
    }

//    @PostMapping("/nuevo")
//    public ResponseEntity<Object> createRequerimiento(@RequestBody RequerimientoDTO requerimientoDTO) {
//        try {
//
//            Requerimiento requerimiento = requerimientoService.crearRequerimiento(requerimientoDTO);
//
//            RequerimientoDTO requerimientoDTORespuesta = requerimientoMapper.toDTO(requerimiento);
//
//            HttpBodyResponse data = new HttpBodyResponse.Builder()
//                    .message("Se ha creado el requerimiento")
//                    .status("Success")
//                    .statusCode(201)
//                    .data(requerimientoDTORespuesta)
//                    .build();
//
//            return ResponseEntity
//                    .status(data.getStatusCode())
//                    .body(data);
//             }catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(400).body("Error: " + e.getMessage());
//             }
//    }

    @PostMapping(value = "/nuevo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createRequerimiento(
            @RequestPart("datos") RequerimientoDTO requerimientoDTO,
            @RequestPart(value = "archivos", required = false) List<MultipartFile> archivos) {
        try {
            Requerimiento requerimiento = requerimientoService.crearRequerimiento(requerimientoDTO, archivos);

            RequerimientoDTO requerimientoDTORespuesta = requerimientoMapper.toDTO(requerimiento);

            HttpBodyResponse data = new HttpBodyResponse.Builder()
                    .message("Se ha creado el requerimiento")
                    .status("Success")
                    .statusCode(201)
                    .data(requerimientoDTORespuesta)
                    .build();

            return ResponseEntity.status(data.getStatusCode()).body(data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/archivoAdjunto/{nombreArchivo}")
    public ResponseEntity<Resource> mostrarArchivo(@PathVariable String nombreArchivo) throws IOException, NotFoundException {

        ArchivoAdjunto archivoAdjunto = archivoAdjuntoService.findByNombre(nombreArchivo);

        if (archivoAdjunto == null) {
            return ResponseEntity.notFound().build();
        }

        Path rutaArchivo = Paths.get(archivoAdjuntoService.getDirectorioArchivos())
                .resolve(archivoAdjunto.getRuta())
                .normalize();

        Resource recurso = new UrlResource(rutaArchivo.toUri());

        if (!recurso.exists() || !recurso.isReadable()) {
            return ResponseEntity.notFound().build();
        }

        String contentType = Files.probeContentType(rutaArchivo);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + nombreArchivo + "\"")
                .body(recurso);
    }

    @PostMapping("/requerimientos/{id}/adjuntar")
      public ResponseEntity<Object> adjuntarArchivos(@PathVariable Long id, @RequestParam("archivos") List<MultipartFile> archivos) {
        try {
            if (archivos.size() > 5) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pueden adjuntar más de 5 archivos.");
           }

            if(archivoAdjuntoService.getArchivosAdjuntosByRequerimientoId(id).size()>=5){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Se ha llegado al limite de archivos adjuntos.");
            }
            List<ArchivoAdjunto> archivosAdjuntos = requerimientoService.adjuntarArchivos(id, archivos);
            List<ArchivoAdjuntoDTO> archivosAdjuntosDTO = archivosAdjuntos.stream()
                    .map(archivoAdjuntoMapper::toDTO)
                    .toList();

             HttpBodyResponse data = new HttpBodyResponse.Builder()
            .message("Archivos adjuntados correctamente")
            .status("Success")
            .statusCode(200)
            .data(archivosAdjuntosDTO)
            .build();

         return ResponseEntity.status(data.getStatusCode()).body(data);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al adjuntar archivos: " + e.getMessage());
        }
    }

    @GetMapping("/requerimientos/{id}/archivos")
    public ResponseEntity<Object> getArchivos(@PathVariable Long id) {
        try{
            List<ArchivoAdjunto> archivosAdjuntos = archivoAdjuntoService.getArchivosAdjuntosByRequerimientoId(id);

            List<ArchivoAdjuntoDTO> archivosAdjuntosDTO = archivosAdjuntos.stream()
                    .map(archivoAdjuntoMapper::toDTO)
                    .toList();

            HttpBodyResponse data = new HttpBodyResponse.Builder()
                    .message("Archivos obtenidos con éxito")
                    .data(archivosAdjuntosDTO)
                    .build();

            return ResponseEntity.status(data.getStatusCode()).body(data);
        }catch (NotFoundException e) {
            return responseFactory.errorNotFound("No existen archivos adjuntos para el requerimiento: " + id);
        } catch (Exception e) {
            return responseFactory.internalServerError();
        }
    }

    @GetMapping("/requerimientos/{idRequerimiento}/archivos/{nombre}")
    public ResponseEntity<Object> getArchivo(@PathVariable Long idRequerimiento, @PathVariable String nombre) {
        try{
            Long idArchivo = archivoAdjuntoService.findByNombre(nombre).getId();
            ArchivoAdjunto archivoAdjunto = requerimientoService.getArchivoAdjuntoById(idRequerimiento, idArchivo);

            ArchivoAdjuntoDTO archivoAdjuntoDTO = archivoAdjuntoMapper.toDTO(archivoAdjunto);

            HttpBodyResponse data = new HttpBodyResponse.Builder()
                    .message("Archivo obtenido con éxito")
                    .data(archivoAdjuntoDTO)
                    .build();

            return ResponseEntity.status(data.getStatusCode()).body(data);
        }catch (NotFoundException e) {
            return responseFactory.errorNotFound("No existe archivo adjunto con el nombre: " + nombre + " para el requerimiento: " + idRequerimiento);
        } catch (Exception e) {
            return responseFactory.internalServerError();
        }
    }

    @DeleteMapping("/requerimientos/{idRequerimiento}/archivos/{nombre}")
    public ResponseEntity<Object> deleteArchivo(@PathVariable Long idRequerimiento, @PathVariable String nombre) {
        try{
            Long idArchivo = archivoAdjuntoService.findByNombre(nombre).getId();
            ArchivoAdjunto archivoAdjunto = requerimientoService.deleteArchivoComentarioById(idRequerimiento, idArchivo);

            ArchivoAdjuntoDTO archivoAdjuntoDTO = archivoAdjuntoMapper.toDTO(archivoAdjunto);

            HttpBodyResponse data = new HttpBodyResponse.Builder()
                    .message("Archivo eliminado con éxito")
                    .data(archivoAdjuntoDTO)
                    .build();

            return ResponseEntity.status(data.getStatusCode()).body(data);
        }catch (NotFoundException e) {
            return responseFactory.errorNotFound("No existe archivo adjunto con el nombre: " + nombre + " para el requerimiento: " + idRequerimiento);
        } catch (Exception e) {
            return responseFactory.internalServerError();
        }
    }
    
}