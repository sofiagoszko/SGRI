package com.api.sgri.repository;

import com.api.sgri.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    List<Comentario> findByRequerimiento_Id(Long requerimientoID);
    List<Comentario> findByUsuarioEmisorComentario_Id(Long usuarioEmisorID);

}
