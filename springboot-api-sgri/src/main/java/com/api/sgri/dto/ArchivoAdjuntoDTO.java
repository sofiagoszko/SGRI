package com.api.sgri.dto;

public class ArchivoAdjuntoDTO {
    private Long id;
    private String nombre;
    private String ruta;
    private Long requerimiento;

    public ArchivoAdjuntoDTO() {

    }

    public ArchivoAdjuntoDTO(String nombre, String ruta) {
        this.nombre = nombre;
        this.ruta = ruta;
    }

    public Long getRequerimiento() {
        return requerimiento;
    }

    public void setRequerimiento(Long requerimiento) {
        this.requerimiento = requerimiento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
