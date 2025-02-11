package com.api.sgri.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    @PostMapping("{id}/comentario")
    public ResponseEntity<Object> createComentario(@PathVariable Long id, @RequestBody ComentarioDTO comentarioDTO) throws NotFoundException {
        try{
            Requerimiento requerimiento = requerimientoService.obtenerRequerimientoPorId(id);
            Comentario comentario = comentarioService.crearComentario(comentarioDTO, requerimiento);

            ComentarioDTO comentarioResponse = comentarioMapper.toDTO(comentario);

            HttpBodyResponse data = new HttpBodyResponse.Builder()
                    .message("Se ha creado el comentario")
                    .status("Success")
                    .statusCode(201)
                    .data(comentarioResponse)
                    .build();

            return ResponseEntity
                    .status(data.getStatusCode())
                    .body(data);


        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/comentarios/{id}/adjuntar")
          public ResponseEntity<Object> adjuntarArchivoComentario(@PathVariable Long id, @RequestParam("archivos") List<MultipartFile> archivos) {
            try {
                if (archivos.size() > 5) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pueden adjuntar más de 5 archivos.");
               }

             Comentario comentario = comentarioService.adjuntarArchivoComentario(id, archivos);

                // Convertir los archivos adjuntos a DTOs
                List<ArchivoComentarioDTO> archivoComentarioDTO = new ArrayList<>();
                 for (ArchivoComentario archivosComentario : comentario.getArchivosComentario()) {
                 archivoComentarioDTO.add(new ArchivoComentarioDTO(archivosComentario.getNombre(), archivosComentario.getRuta()));
                 }

                    // Retornar solo los DTOs de los archivos
                 HttpBodyResponse data = new HttpBodyResponse.Builder()
                .message("Archivos adjuntados correctamente")
                .status("Success")
                .statusCode(200)
                .data(archivoComentarioDTO)
                .build();

             return ResponseEntity.status(data.getStatusCode()).body(data);

         } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al adjuntar archivos: " + e.getMessage());
     }
    }

}
