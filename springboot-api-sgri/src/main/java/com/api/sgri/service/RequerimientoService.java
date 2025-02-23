package com.api.sgri.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


import com.api.sgri.model.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.api.sgri.dto.RequerimientoDTO;
import com.api.sgri.exception.NotFoundException;
import com.api.sgri.mapper.RequerimientoMapper;

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
    private TipoRequerimientoRepository tipoRequerimientoRepository;

    @Autowired
    private RequerimientoMapper requerimientoMapper;

    @Autowired
    private ArchivoAdjuntoService archivoAdjuntoService;

    public Requerimiento crearRequerimiento(RequerimientoDTO dto, List<MultipartFile> archivos) throws NotFoundException, IOException, RuntimeException {

        TipoRequerimiento tipoRequerimiento = tipoRequerimientoRepository.findById(dto.getTipoRequerimiento())
                .orElseThrow(() -> new NotFoundException("Tipo de Requerimiento no encontrado"));

        UsuarioEmpresa usuarioEmisor = usuarioEmpresaRepository.findById(dto.getUsuarioEmisor())
                .orElseThrow(() -> new NotFoundException("Usuario Emisor no encontrado"));

        UsuarioEmpresa usuarioDestinatario = (dto.getUsuarioDestinatario() != null)
                ? usuarioEmpresaRepository.findById(dto.getUsuarioDestinatario()).orElse(null)
                : null;

        Requerimiento requerimiento = requerimientoMapper.fromDTO(dto, tipoRequerimiento, usuarioEmisor, usuarioDestinatario, new ArrayList<>());

        if (archivos != null && !archivos.isEmpty() && archivos.size()<=5) {
            List<ArchivoAdjunto> archivosAdjuntos = new ArrayList<>();
            for (MultipartFile archivo : archivos) {
                String rutaArchivo = archivoAdjuntoService.guardarArchivo(archivo);
                ArchivoAdjunto archivoAdjunto = new ArchivoAdjunto();
                archivoAdjunto.setNombre(archivo.getOriginalFilename());
                archivoAdjunto.setRuta(rutaArchivo);
                archivoAdjunto.setRequerimiento(requerimiento);
                archivosAdjuntos.add(archivoAdjunto);
            }
            requerimiento.setArchivosAdjuntos(archivosAdjuntos);
        }else if (archivos != null && archivos.size()>5) {
            throw new RuntimeException("No se pueden adjuntar más de 5 archivos.");
        }

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

    public List<RequerimientoDTO> obtenerRequerimientosPorIdUsuario(Long id) throws NotFoundException {
        return requerimientoRepository.findByUsuarioDestinatario_Id(id).stream()
        .map(requerimientoMapper::toDTO)
        .collect(Collectors.toList());
    }

    public List<RequerimientoDTO> obtenerRequerimientosPorIdUsuarioEmisor(Long id) throws NotFoundException {
        return requerimientoRepository.findByUsuarioEmisor_Id(id).stream()
        .map(requerimientoMapper::toDTO)
        .collect(Collectors.toList());
    }

    public RequerimientoDTO updateRequerimiento(Requerimiento requerimiento, RequerimientoDTO requerimientoDTO) throws NotFoundException {
        requerimiento.setAsunto(requerimientoDTO.getAsunto());
        requerimiento.setDescripcion(requerimientoDTO.getDescripcion());
        requerimiento.setEstado(requerimientoDTO.getEstado());
        requerimiento.setFechaHora(requerimientoDTO.getFechaHora());
        requerimiento.setPrioridad(requerimientoDTO.getPrioridad());
        requerimiento.setCategoria(requerimientoDTO.getCategoriaTipo());

        TipoRequerimiento tipoRequerimiento = tipoRequerimientoRepository.findById(requerimientoDTO.getTipoRequerimiento())
                .orElseThrow(() -> new NotFoundException("Tipo de Requerimiento no encontrado"));
        requerimiento.setTipoRequerimiento(tipoRequerimiento);

        UsuarioEmpresa usuarioEmisor = usuarioEmpresaRepository.findById(requerimientoDTO.getUsuarioEmisor())
                .orElseThrow(() -> new NotFoundException("Usuario Emisor no encontrado"));
        requerimiento.setUsuarioEmisor(usuarioEmisor);

        UsuarioEmpresa usuarioDestinatario = requerimientoDTO.getUsuarioDestinatario() != null ? usuarioEmpresaRepository.findById(requerimientoDTO.getUsuarioDestinatario()).orElse(null) : null;
        requerimiento.setUsuarioDestinatario(usuarioDestinatario);

        if (requerimientoDTO.getCodigo() != null) {
            requerimiento.setCodigo(requerimientoDTO.getCodigo());
        }

        requerimiento = requerimientoRepository.save(requerimiento);
        return requerimientoMapper.toDTO(requerimiento);
    }


    public List<ArchivoAdjunto> adjuntarArchivos(Long requerimientoId, List<MultipartFile> archivos) throws IOException {
        Requerimiento requerimiento = requerimientoRepository.findById(requerimientoId)
                .orElseThrow(() -> new RuntimeException("Requerimiento no encontrado"));

        if (archivos.size() > 5) {
            throw new RuntimeException("No se pueden adjuntar más de 5 archivos.");
        }

        List<ArchivoAdjunto> archivosCargados = new ArrayList<>();
        for (MultipartFile archivo : archivos) {
            String rutaArchivo = archivoAdjuntoService.guardarArchivo(archivo);
            ArchivoAdjunto archivoAdjunto = new ArchivoAdjunto();
            archivoAdjunto.setNombre(archivo.getOriginalFilename());
            archivoAdjunto.setRuta(rutaArchivo);
            archivoAdjunto.setRequerimiento(requerimiento);

            archivosCargados.add(archivoAdjunto);
            requerimiento.getArchivosAdjuntos().add(archivoAdjunto);
        }

        requerimientoRepository.save(requerimiento);
        return archivosCargados;
    }

    public ArchivoAdjunto getArchivoAdjuntoById(Long requerimientoId, Long archivoId) throws NotFoundException {
        Requerimiento requerimiento = requerimientoRepository.findById(requerimientoId)
                .orElseThrow(() -> new RuntimeException("Requerimiento no encontrado"));

        return archivoAdjuntoService.getArchivoAdjuntoById(archivoId);
    }

    public ArchivoAdjunto deleteArchivoComentarioById(Long requerimientoId, Long archivoId) throws NotFoundException {
        Requerimiento requerimiento = requerimientoRepository.findById(requerimientoId)
                .orElseThrow(() -> new RuntimeException("Requerimiento no encontrado"));

        return archivoAdjuntoService.deleteArchivoAdjuntoById(archivoId);
    }
}
