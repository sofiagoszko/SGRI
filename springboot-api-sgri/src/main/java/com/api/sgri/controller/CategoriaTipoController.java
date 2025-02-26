package com.api.sgri.controller;

import com.api.sgri.model.CategoriaTipo;
import com.api.sgri.service.CategoriaTipoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/categoria")
@CrossOrigin(origins = "*")
public class CategoriaTipoController {

    @Autowired
    private CategoriaTipoService categoriaTipoService;

    @GetMapping("/todas")
    public ResponseEntity<List<CategoriaTipo>> getCategorias() {
        return ResponseEntity.ok(categoriaTipoService.obtenerCategorias());
    }

    @GetMapping("/tipo/{id}")
    public ResponseEntity<List<CategoriaTipo>> getCategoriasPorTipo(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaTipoService.obtenerCategoriasPorTipo(id));
    }

    @PostMapping("/nueva")
    public ResponseEntity<CategoriaTipo> createCategoria(@RequestBody Map<String, Object> request) {
        String descripcion = (String) request.get("descripcion");
        Long tipoRequerimientoId = ((Number) request.get("tipoRequerimientoId")).longValue();

        CategoriaTipo nuevaCategoria = categoriaTipoService.crearCategoria(descripcion, tipoRequerimientoId);
        return ResponseEntity.ok(nuevaCategoria);
    }
}
