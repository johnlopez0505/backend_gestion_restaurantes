package com.john.backend_gestion_restaurantes.servicios.calificacion;

import java.util.List;
import java.util.Optional;

import com.john.backend_gestion_restaurantes.dto.CalificacionDTO;
import com.john.backend_gestion_restaurantes.modelos.Calificacion;

public interface CalificacionService {
    
    List<CalificacionDTO> findAllCalificacion();
    Optional<CalificacionDTO> findById(Integer id);
    Optional<Calificacion> findCalificacionById(Integer id);
    Optional<CalificacionDTO> saveCalificacion(Calificacion calificacion);
    void deleteCalificacionById(Integer id);
    void deleteAll();
    List<CalificacionDTO> getCalificacionesCreatedByUser(String userId);
}
