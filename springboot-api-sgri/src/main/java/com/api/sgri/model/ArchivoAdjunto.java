package com.api.sgri.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "ArchivosAdjuntos")
public class ArchivoAdjunto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Lob
    @Column(columnDefinition = "LONGLOB", name = "archivo")
    private byte[] archivo;

}
