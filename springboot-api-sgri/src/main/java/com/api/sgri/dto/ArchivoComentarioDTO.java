package com.api.sgri.dto;

import lombok.Data;

@Data
public class ArchivoComentarioDTO {

    private Long id;
    private String nombre;
    private String ruta;
    private Long comentario;


    public ArchivoComentarioDTO() {}

    public ArchivoComentarioDTO(String nombre, String ruta) {
        this.nombre = nombre;
        this.ruta = ruta;
    }

}
