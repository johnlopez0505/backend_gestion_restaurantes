package com.john.backend_gestion_restaurantes.servicios.calificacion;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.john.backend_gestion_restaurantes.dto.CalificacionDTO;
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
    public List<CalificacionDTO> findAllCalificacion() {
        System.out.println("entramos en finAll()");
        List<Calificacion> calificaciones = repoCalificacion.findAll();
        System.out.println("estas son las calificaciones: " + calificaciones);
        return calificaciones.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

   @Override
    public Optional<CalificacionDTO> findById(Integer id) {
        Optional<Calificacion> calificacion = repoCalificacion.findById(id);
        return calificacion.map(this::convertToDTO);
    }


    @Override
    public Optional<Calificacion> findCalificacionById(Integer id) {
        return repoCalificacion.findById(id);
    }

    @Override
    public Optional<CalificacionDTO> saveCalificacion(Calificacion calificacion) {
        try {
            Calificacion guardarCalificacion = repoCalificacion.save(calificacion);
            return Optional.ofNullable(convertToDTO(guardarCalificacion));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteCalificacionById(Integer id) {
        this.repoCalificacion.deleteById(id);
    }

    @Override
    public void deleteAll() {
      this.repoCalificacion.deleteAll();
    }

    @Override
    public List<CalificacionDTO> getCalificacionesCreatedByUser(String userId) {
        List<Calificacion> calificaciones = repoCalificacion.findByCreatedBy(userId);
        return calificaciones.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private CalificacionDTO convertToDTO(Calificacion calificacion) {
        System.out.println("entramos en dto");
        CalificacionDTO dto = new CalificacionDTO();
        dto.setId(calificacion.getId());
        dto.setUsuarioId(calificacion.getUsuario().getId());
        dto.setRestauranteId(calificacion.getRestaurante().getId());
        dto.setPuntuacion(calificacion.getPuntuacion());
        dto.setComentario(calificacion.getComentario());
        // Usar el operador ternario para manejar el caso en que createdAt es nulo
        String fechaYHoraStr = (calificacion.getCreatedAt() != null) 
        ? calificacion.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) 
        : "";
        dto.setCreatedAt(fechaYHoraStr);
        return dto;
    }

}
