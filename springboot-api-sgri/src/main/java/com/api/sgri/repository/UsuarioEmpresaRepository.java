package com.api.sgri.repository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.api.sgri.model.UsuarioEmpresa;

@Repository
public interface UsuarioEmpresaRepository extends JpaRepository<UsuarioEmpresa, Long> {
    Optional<UsuarioEmpresa> findById(Long usuarioEmpresaId);

    Optional<UsuarioEmpresa> findByUserName(String userName);
    boolean existsByUserName(String userName);

    Optional<UsuarioEmpresa> findByLegajo(int legajo);
    boolean existsByLegajo(int legajo);

    Optional<UsuarioEmpresa> findByEmail(String email);
    boolean existsByEmail(String email);

    boolean existsByEmailOrUserNameOrLegajo(@Param("email") String email, @Param("userName") String userName, @Param("legajo") int legajo);
}