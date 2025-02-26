package com.api.sgri.controller;

import java.util.List;

import com.api.sgri.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.sgri.dto.CategoriaTipoDTO;
import com.api.sgri.dto.TipoRequerimientoDTO;
import com.api.sgri.mapper.TipoRequerimientoMapper;
import com.api.sgri.model.CategoriaTipo;
import com.api.sgri.model.TipoRequerimiento;
import com.api.sgri.response.HttpBodyResponse;
import com.api.sgri.response.ResponseFactory;
import com.api.sgri.service.TipoRequerimientoService;

@RestController
@RequestMapping("api/tipo-requerimiento")
public class TipoRequerimientoController {

    @Autowired
    private TipoRequerimientoService tipoRequerimientoService;
    @Autowired
    private ResponseFactory responseFactory;
    @Autowired
    private TipoRequerimientoMapper tipoRequerimientoMapper;

    @GetMapping("/{id}/categorias")
    public ResponseEntity<List<CategoriaTipo>> getCategoriasPorTipo(@PathVariable Long id) {
        List<CategoriaTipo> categorias = tipoRequerimientoService.getCategorias(id);
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/tipos")
    public ResponseEntity<Object> getAll(@RequestParam(required = false) String param) {
        List<TipoRequerimientoDTO> tiposRequerimiento = tipoRequerimientoService.getTipoRequerimientos();
        return ResponseEntity.ok(tiposRequerimiento);
    }

    @GetMapping("/tipos/{id}")
    public ResponseEntity<Object> getTipoDeRequerimiento(@PathVariable Long id){
        try {
            TipoRequerimiento tipoRequerimiento = tipoRequerimientoService.getTipoRequerimentoById(id);
            TipoRequerimientoDTO  tipoRequerimientoDTO = tipoRequerimientoMapper.toDTO(tipoRequerimiento);

            HttpBodyResponse data = new HttpBodyResponse.Builder()
                    .message("Tipo de requerimiento obtenido con éxito")
                    .data(tipoRequerimientoDTO)
                    .build();

            return ResponseEntity.status(data.getStatusCode()).body(data);
        } catch (NotFoundException e) {
            return responseFactory.errorNotFound("No existe tipo de requerimiento con id: " + id);
        } catch (Exception e) {
            return responseFactory.internalServerError();
        }

    }

    @PostMapping()
    public ResponseEntity<Object> createTipoRequerimiento(@RequestBody TipoRequerimientoDTO dto) {
        try {
            TipoRequerimientoMapper tipoMapper = new TipoRequerimientoMapper();
            TipoRequerimiento tipoRequerimiento = tipoMapper.fromDTO(dto);

            tipoRequerimientoService.crearTipoRequerimiento(tipoRequerimiento);

            HttpBodyResponse data = new HttpBodyResponse.Builder()
                    .status("Success")
                    .statusCode(200)
                    .message("Se ha creado un nuevo tipo de requerimiento")
                    .data(tipoRequerimiento)
                    .build();

            return ResponseEntity
                    .status(data.getStatusCode())
                    .body(data);

        } catch (Exception e) {
            return responseFactory.internalServerError();
        }
    }

    @PostMapping("/{id}/categoria")
    public ResponseEntity<Object> createCategoria(@PathVariable Long id, @RequestBody CategoriaTipoDTO categoriaDTO) {
        try {

            TipoRequerimiento tipoRequerimiento = tipoRequerimientoService.getTipoRequerimentoById(id);

            CategoriaTipo categoria = new CategoriaTipo();
            categoria.setDescripcion(categoriaDTO.getDescripcion());
            categoria.setTipoRequerimiento(tipoRequerimiento);

            CategoriaTipo categoriaGuardada = tipoRequerimientoService.crearCategoriaTipo(categoria);

            HttpBodyResponse data = new HttpBodyResponse.Builder()
                    .status("Success")
                    .statusCode(200)
                    .message("Se ha agregado una nueva categoría")
                    .data(categoriaGuardada)
                    .build();

            return ResponseEntity
                    .status(data.getStatusCode())
                    .body(data);
        } catch (Exception e) {
            return responseFactory.internalServerError();
        }
    }

}
