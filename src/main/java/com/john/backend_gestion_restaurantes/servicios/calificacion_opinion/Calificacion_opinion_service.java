package com.john.backend_gestion_restaurantes.servicios.calificacion_opinion;

import java.util.List;
import java.util.Optional;

import com.john.backend_gestion_restaurantes.modelos.CalificacionOpinion;

public interface Calificacion_opinion_service {
    
    List<CalificacionOpinion> findAllCalificacionOpinions();
    Optional<CalificacionOpinion> findCalificacionOpinionById(Integer id);
    CalificacionOpinion saveCalificacionOpinion(CalificacionOpinion calificacionOpinion);
    void deleteCalificacionOpinionById(Integer id);
}
