package com.john.backend_gestion_restaurantes.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.john.backend_gestion_restaurantes.modelos.Restaurante;

@Repository
public interface RepoRestaurante extends JpaRepository<Restaurante,Integer>{
    // Método para buscar los restaurantes según el nombre de usuario que la creo
    List<Restaurante> findByCreatedBy(String createdBy);
}
