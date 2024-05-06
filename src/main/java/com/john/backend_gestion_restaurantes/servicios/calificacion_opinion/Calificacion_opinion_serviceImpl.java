package com.john.backend_gestion_restaurantes.servicios.calificacion_opinion;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.john.backend_gestion_restaurantes.modelos.CalificacionOpinion;
import com.john.backend_gestion_restaurantes.repositorios.RepoCalificacionOpinion;

@Service
public class Calificacion_opinion_serviceImpl implements Calificacion_opinion_service{

    private RepoCalificacionOpinion repoCalificacionOpinion;

    public Calificacion_opinion_serviceImpl(RepoCalificacionOpinion repoCalificacionOpinion){
        this.repoCalificacionOpinion = repoCalificacionOpinion;
    }

    @Override
    public List<CalificacionOpinion> findAllCalificacionOpinions() {
        return repoCalificacionOpinion.findAll();
    }

    @Override
    public Optional<CalificacionOpinion> findCalificacionOpinionById(Integer id) {
        return repoCalificacionOpinion.findById(id);
    }

    @Override
    public CalificacionOpinion saveCalificacionOpinion(CalificacionOpinion calificacionOpinion) {
        return repoCalificacionOpinion.save(calificacionOpinion);
    }

    @Override
    public void deleteCalificacionOpinionById(Integer id) {
       repoCalificacionOpinion.deleteById(id);
    }
}
