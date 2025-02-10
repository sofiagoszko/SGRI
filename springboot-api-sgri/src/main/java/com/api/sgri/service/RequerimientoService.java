package com.api.sgri.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.api.sgri.dto.RequerimientoDTO;
import com.api.sgri.exception.NotFoundException;
import com.api.sgri.mapper.RequerimientoMapper;
import com.api.sgri.model.ArchivoAdjunto;
import com.api.sgri.model.Requerimiento;
import com.api.sgri.model.TipoRequerimiento;
import com.api.sgri.model.UsuarioEmpresa;
import com.api.sgri.repository.CategoriaTipoRepository;
import com.api.sgri.repository.RequerimientoRepository;
import com.api.sgri.repository.TipoRequerimientoRepository;
import com.api.sgri.repository.UsuarioEmpresaRepository;



@Service
public class RequerimientoService {
    @Autowired
    private RequerimientoRepository requerimientoRepository;

    @Autowired
    private UsuarioEmpresaRepository usuarioEmpresaRepository;

    @Autowired
    private CategoriaTipoRepository categoriaTipoRepository;


    @Autowired
    private TipoRequerimientoRepository tipoRequerimientoRepository;

    @Autowired
    private RequerimientoMapper requerimientoMapper;

    @Autowired
    private ArchivoAdjuntoService archivoAdjuntoService;

    public Requerimiento crearRequerimiento(RequerimientoDTO dto) throws NotFoundException {

        TipoRequerimiento tipoRequerimiento = tipoRequerimientoRepository.findById(dto.getTipoRequerimiento())
                .orElseThrow(() -> new NotFoundException("Tipo de Requerimiento no encontrado"));

        UsuarioEmpresa usuarioEmisor = usuarioEmpresaRepository.findById(dto.getUsuarioEmisor())
                .orElseThrow(() -> new NotFoundException("Usuario Emisor no encontrado"));
        // Validar existencia de usuario destinatario (opcional)
        UsuarioEmpresa usuarioDestinatario = dto.getUsuarioDestinatario() != null ? usuarioEmpresaRepository.findById(dto.getUsuarioDestinatario()).orElse(null) : null;

        // Validar existencia de la categoria
        //CategoriaTipo categoria = categoriaTipoRepository.findById(dto.getCategoriaTipo().getId())
        //.orElseThrow(() -> new NotFoundException("Categoría no encontrada"));

        // Crear el Requerimiento
        Requerimiento requerimiento = requerimientoMapper.fromDTO(dto, tipoRequerimiento, usuarioEmisor, usuarioDestinatario);
        return requerimientoRepository.save(requerimiento);
    }

    public List<RequerimientoDTO> obtenerRequerimientos() {
        return requerimientoRepository.findAll().stream()
                .map(requerimientoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Requerimiento obtenerRequerimientoPorId(Long id) throws NotFoundException {
        Requerimiento requerimiento = requerimientoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Requerimiento no encontrado"));
        return requerimiento;
    }


    public Requerimiento adjuntarArchivos(Long requerimientoId, List<MultipartFile> archivos) throws IOException {
        Requerimiento requerimiento = requerimientoRepository.findById(requerimientoId)
                .orElseThrow(() -> new RuntimeException("Requerimiento no encontrado"));

        if (archivos.size() > 5) {
            throw new RuntimeException("No se pueden adjuntar más de 5 archivos.");
        }

        for (MultipartFile archivo : archivos) {
            String rutaArchivo = archivoAdjuntoService.guardarArchivo(archivo); // Guardamos el archivo
            ArchivoAdjunto archivoAdjunto = new ArchivoAdjunto();
            archivoAdjunto.setNombre(archivo.getOriginalFilename());
            archivoAdjunto.setRuta(rutaArchivo);
            archivoAdjunto.setRequerimiento(requerimiento);

            requerimiento.getArchivosAdjuntos().add(archivoAdjunto); // Asociamos el archivo al requerimiento
        }

        return requerimientoRepository.save(requerimiento);  // Guardamos el requerimiento con los archivos adjuntos
    }
}
