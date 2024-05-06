package com.john.backend_gestion_restaurantes.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.john.backend_gestion_restaurantes.modelos.Usuario;

@Repository
public interface RepoUsuarios extends JpaRepository<Usuario, Integer>{
    Optional<Usuario> findFirstByUsername(String username);
} 