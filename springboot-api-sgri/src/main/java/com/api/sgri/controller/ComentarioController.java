package com.api.sgri.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;



import com.api.sgri.mapper.ArchivoComentarioMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.api.sgri.dto.ArchivoComentarioDTO;
import com.api.sgri.dto.ComentarioDTO;
import com.api.sgri.exception.NotFoundException;
import com.api.sgri.mapper.ComentarioMapper;
import com.api.sgri.model.ArchivoComentario;
import com.api.sgri.model.Comentario;
import com.api.sgri.model.Requerimiento;
import com.api.sgri.response.HttpBodyResponse;
import com.api.sgri.response.ResponseFactory;
import com.api.sgri.service.ArchivoComentarioService;
import com.api.sgri.service.ComentarioService;
import com.api.sgri.service.RequerimientoService;

@RestController
@RequestMapping("api/requerimiento")
@CrossOrigin(origins = "*")
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;

    @Autowired
    private ComentarioMapper comentarioMapper;

    @Autowired
    private RequerimientoService requerimientoService;

    @Autowired
    private ResponseFactory responseFactory;

    @Autowired
    private ArchivoComentarioService archivoComentarioService;

    @Autowired
    private ArchivoComentarioMapper archivoComentarioMapper;


    @GetMapping("comentario/{id}")
    public ResponseEntity<Object> getComentario(@PathVariable Long id) throws NotFoundException {
        try{
            Comentario comentario = comentarioService.getComentarioById(id);

            ComentarioDTO comentarioDTO = comentarioMapper.toDTO(comentario);

            // Crear la respuesta
            HttpBodyResponse data = new HttpBodyResponse.Builder()
                    .message("Comentario obtenido con éxito")
                    .data(comentarioDTO)
                    .build();

            return ResponseEntity.status(data.getStatusCode()).body(data);
        }catch (NotFoundException e) {
            return responseFactory.errorNotFound("No existe comentario con id: " + id);
        } catch (Exception e) {
            return responseFactory.internalServerError();
        }
    }

    @GetMapping("/{id}/comentarios")
    public ResponseEntity<Object> getComentariosByRequerimiento(@PathVariable Long id) throws NotFoundException {
        try{
            List<Comentario> comentarios = comentarioService.getComentariosByRequerimiento(id);

            List<ComentarioDTO> comentariosDTO = comentarios.stream()
                    .map(comentarioMapper::toDTO)
                    .toList();

            HttpBodyResponse data = new HttpBodyResponse.Builder()
                    .message("Comentarios obtenido con éxito")
                    .data(comentariosDTO)
                    .build();

            return ResponseEntity.status(data.getStatusCode()).body(data);
        }catch (NotFoundException e) {
            return responseFactory.errorNotFound("No existen comentarios para el requerimiento: " + id);
        } catch (Exception e) {
            return responseFactory.internalServerError();
        }
    }

    @GetMapping("comentario/usuario/{id}")
    public ResponseEntity<Object> getComentariosByUsuarioEmisor(@PathVariable Long id) throws NotFoundException {
        try{
            List<Comentario> comentarios = comentarioService.getComentariosByUsuarioEmisor(id);

            List<ComentarioDTO> comentariosDTO = comentarios.stream()
                    .map(comentarioMapper::toDTO)
                    .toList();

            HttpBodyResponse data = new HttpBodyResponse.Builder()
                    .message("Comentarios obtenido con éxito")
                    .data(comentariosDTO)
                    .build();

            return ResponseEntity.status(data.getStatusCode()).body(data);
        }catch (NotFoundException e) {
            return responseFactory.errorNotFound("No existen comentarios hechos por el usuario: " + id);
        } catch (Exception e) {
            return responseFactory.internalServerError();
        }
    }

    @PostMapping(value="{id}/comentario", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createComentario(@PathVariable Long id,
                                                   @RequestPart("datos") ComentarioDTO comentarioDTO,
                                                   @RequestPart(value = "archivos", required = false) List<MultipartFile> archivos) throws NotFoundException {
        try{
            Requerimiento requerimiento = requerimientoService.obtenerRequerimientoPorId(id);
            Comentario comentario = comentarioService.crearComentario(comentarioDTO, requerimiento, archivos);

            ComentarioDTO comentarioDTORespuesta = comentarioMapper.toDTO(comentario);

            HttpBodyResponse data = new HttpBodyResponse.Builder()
                    .message("Se ha creado el comentario")
                    .status("Success")
                    .statusCode(201)
                    .data(comentarioDTORespuesta)
                    .build();

            return ResponseEntity.status(data.getStatusCode()).body(data);

        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/comentarios/{id}/adjuntar")
    public ResponseEntity<Object> adjuntarArchivoComentario(@PathVariable Long id, @RequestParam("archivos") List<MultipartFile> archivos) {
        try {
            getComentario(id);
            if (archivos.size() > 5) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pueden adjuntar más de 5 archivos.");
            }

            if(!archivoComentarioService.getArchivosAdjuntosByComentarioId(id).isEmpty() && archivoComentarioService.getArchivosAdjuntosByComentarioId(id).size()>=5){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Se ha llegado al limite de archivos adjuntos.");
            }

            List<ArchivoComentario> archivosComentario = comentarioService.adjuntarArchivoComentario(id, archivos);

            List<ArchivoComentarioDTO> archivosComentarioDTO = archivosComentario.stream()
                    .map(archivoComentarioMapper::toDTO)
                    .toList();

            HttpBodyResponse data = new HttpBodyResponse.Builder()
                    .message("Archivo adjuntado correctamente")
                    .status("Success")
                    .statusCode(200)
                    .data(archivosComentarioDTO)
                    .build();

            return ResponseEntity.status(data.getStatusCode()).body(data);
        } catch (NotFoundException e) {
            return responseFactory.errorNotFound("No existe comentario con id: " + id);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al adjuntar archivos: " + e.getMessage());
        }
    }

    @GetMapping("/archivoComentario/{nombreArchivo}")
    public ResponseEntity<Resource> mostrarArchivo(@PathVariable String nombreArchivo) throws IOException, NotFoundException {

        ArchivoComentario archivoComentario = archivoComentarioService.findByNombre(nombreArchivo);

        if (archivoComentario  == null) {
            return ResponseEntity.notFound().build();
        }

        Path rutaArchivo = Paths.get(archivoComentarioService.getDirectorioArchivos())
                .resolve(archivoComentario.getRuta())
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

    @GetMapping("/comentarios/{id}/archivos")
    public ResponseEntity<Object> getArchivos(@PathVariable Long id) {
        try{
            List<ArchivoComentario> archivoComentarios = archivoComentarioService.getArchivosAdjuntosByComentarioId(id);


            List<ArchivoComentarioDTO> archivosComentarioDTO = archivoComentarios.stream()
                    .map(archivoComentarioMapper::toDTO)
                    .toList();

            HttpBodyResponse data = new HttpBodyResponse.Builder()
                    .message("Archivos obtenidos con éxito")
                    .data(archivosComentarioDTO)
                    .build();

            return ResponseEntity.status(data.getStatusCode()).body(data);
        }catch (NotFoundException e) {
            return responseFactory.errorNotFound("No existen archivos adjuntos pora el comentario: " + id);
        } catch (Exception e) {
            return responseFactory.internalServerError();
        }
    }

    @GetMapping("/comentarios/{idComentario}/archivos/{nombre}")
    public ResponseEntity<Object> getArchivo(@PathVariable Long idComentario, @PathVariable String nombre) {
        try {
            //ArchivoComentario archivoComentario = archivoComentarioService.getArchivoComentarioById(idComentario);
            Long idArchivo = archivoComentarioService.findByNombre(nombre).getId();
            ArchivoComentario archivoComentario = comentarioService.getArchivoComentarioById(idComentario, idArchivo);

            ArchivoComentarioDTO archivoComentarioDTO = archivoComentarioMapper.toDTO(archivoComentario);

            HttpBodyResponse data = new HttpBodyResponse.Builder()
                    .message("Archivo obtenido con éxito")
                    .data(archivoComentarioDTO)
                    .build();

            return ResponseEntity.status(data.getStatusCode()).body(data);
        }catch (NotFoundException e) {
            return responseFactory.errorNotFound("No existe el archivo adjunto con el nombre  " + nombre + " para el comentario con id: " + idComentario);
        } catch (Exception e) {
            return responseFactory.internalServerError();
        }
    }

    @DeleteMapping("/comentarios/{idComentario}/archivos/{nombre}")
    public ResponseEntity<Object> deleteArchivo(@PathVariable Long idComentario, @PathVariable String nombre) {
        try {
            Long idArchivo = archivoComentarioService.findByNombre(nombre).getId();
            ArchivoComentario archivoComentario = comentarioService.deleteArchivoComentarioById(idComentario, idArchivo);

            ArchivoComentarioDTO archivoComentarioDTO = archivoComentarioMapper.toDTO(archivoComentario);

            HttpBodyResponse data = new HttpBodyResponse.Builder()
                    .message("Archivo eliminado con éxito")
                    .data(archivoComentarioDTO)
                    .build();

            return ResponseEntity.status(data.getStatusCode()).body(data);
        }catch (NotFoundException e) {
            return responseFactory.errorNotFound("No existe el archivo adjunto con el nombre  " + nombre + " para el comentario con id: " + idComentario);
        } catch (Exception e) {
            return responseFactory.internalServerError();
        }
    }

}
