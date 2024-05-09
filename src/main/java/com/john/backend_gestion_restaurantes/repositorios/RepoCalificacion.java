package com.john.backend_gestion_restaurantes.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.john.backend_gestion_restaurantes.modelos.Calificacion;

@Repository
public interface RepoCalificacion extends JpaRepository<Calificacion, Integer>{
    // Método para buscar calificacion según el nombre de usuario del creador
    List<Calificacion> findByCreatedBy(String createdBy);
}
