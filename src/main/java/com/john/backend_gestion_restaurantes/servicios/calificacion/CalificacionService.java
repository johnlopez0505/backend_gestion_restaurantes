package com.john.backend_gestion_restaurantes.servicios.calificacion;

import java.util.List;
import java.util.Optional;

import com.john.backend_gestion_restaurantes.modelos.Calificacion;

public interface CalificacionService {
    
    List<Calificacion> findAllCalificacion();
    List<Calificacion> getCalificacionesCreatedByUser(String username);
    Optional<Calificacion> findCalificacionById(Integer id);
    Calificacion saveCalificacion(Calificacion calificacion);
    void deleteCalificacionById(Integer id);
}
