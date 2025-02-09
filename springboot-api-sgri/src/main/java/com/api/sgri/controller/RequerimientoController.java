package com.api.sgri.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.api.sgri.dto.RequerimientoDTO;
import com.api.sgri.dto.TipoRequerimientoDTO;
import com.api.sgri.dto.UsuarioEmpresaDTO;
import com.api.sgri.exception.DuplicateUserException;
import com.api.sgri.mapper.TipoRequerimientoMapper;
import com.api.sgri.mapper.RequerimientoMapper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.sgri.dto.RequerimientoDTO;
import com.api.sgri.dto.UsuarioEmpresaDTO;
import com.api.sgri.exception.DuplicateRequerimientoException;
import com.api.sgri.exception.NotFoundException;

import com.api.sgri.model.Requerimiento;
import com.api.sgri.model.TipoRequerimiento;
import com.api.sgri.model.UsuarioEmpresa;
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

            // Crear la respuesta
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

    @PostMapping("/nuevo")
    public ResponseEntity<Object> createRequerimiento(@RequestBody RequerimientoDTO requerimientoDTO) {
        try {

            Requerimiento requerimiento = requerimientoService.crearRequerimiento(requerimientoDTO);

            HttpBodyResponse data = new HttpBodyResponse.Builder()
                    .message("Se ha creado el requerimiento")
                    .status("Success")
                    .statusCode(201)
                    .data(requerimientoDTO)
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