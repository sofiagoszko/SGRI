package com.api.sgri.dto;

public class ArchivoComentarioDTO {

    private String nombre;
    private String ruta;

    // Constructor
    public ArchivoComentarioDTO(String nombre, String ruta) {
        this.nombre = nombre;
        this.ruta = ruta;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
}
