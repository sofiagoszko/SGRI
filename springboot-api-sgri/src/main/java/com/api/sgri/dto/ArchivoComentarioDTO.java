package com.api.sgri.dto;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getComentario() {
        return comentario;
    }

    public void setComentario(Long comentario) {
        this.comentario = comentario;
    }

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
