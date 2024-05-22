package com.john.backend_gestion_restaurantes.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.john.backend_gestion_restaurantes.modelos.Reserva;

@Repository
public interface RepoReservas extends JpaRepository<Reserva, Integer> {
    // Método para buscar las reservas según el nombre de usuario que la creo
    List<Reserva> findByCreatedBy(String createdBy);
}
