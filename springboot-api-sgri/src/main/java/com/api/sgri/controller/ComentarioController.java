package com.api.sgri.controller;

import com.api.sgri.dto.ComentarioDTO;
import com.api.sgri.dto.UsuarioEmpresaDTO;
import com.api.sgri.exception.NotFoundException;
import com.api.sgri.mapper.ComentarioMapper;
import com.api.sgri.mapper.UsuarioEmpresaMapper;
import com.api.sgri.model.Comentario;
import com.api.sgri.model.Requerimiento;
import com.api.sgri.model.UsuarioEmpresa;
import com.api.sgri.response.HttpBodyResponse;
import com.api.sgri.response.ResponseFactory;
import com.api.sgri.service.ComentarioService;
import com.api.sgri.service.RequerimientoService;
import com.api.sgri.service.UsuarioEmpresaService;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

}
