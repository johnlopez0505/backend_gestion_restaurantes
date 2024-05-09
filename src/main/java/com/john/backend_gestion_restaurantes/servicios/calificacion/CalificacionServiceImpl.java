package com.john.backend_gestion_restaurantes.servicios.calificacion;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.john.backend_gestion_restaurantes.modelos.Calificacion;
import com.john.backend_gestion_restaurantes.repositorios.RepoCalificacion;

@Service
public class CalificacionServiceImpl implements CalificacionService{

    @Autowired
    private RepoCalificacion repoCalificacion;

    public CalificacionServiceImpl(RepoCalificacion repoCalificacion){
        this.repoCalificacion = repoCalificacion;
    }

    @Override
    public List<Calificacion> findAllCalificacion() {
        return repoCalificacion.findAll();
    }

    @Override
    public Optional<Calificacion> findCalificacionById(Integer id) {
        return repoCalificacion.findById(id);
    }

    @Override
    public Calificacion saveCalificacion(Calificacion calificacion) {
        return repoCalificacion.save(calificacion);
    }

    @Override
    public void deleteCalificacionById(Integer id) {
       repoCalificacion.deleteById(id);
    }

    @Override
    public List<Calificacion> getCalificacionesCreatedByUser(String username) {
        // Consulta las calificaciones creadas por el usuario 
        return repoCalificacion.findByCreatedBy(username);
    }
}
