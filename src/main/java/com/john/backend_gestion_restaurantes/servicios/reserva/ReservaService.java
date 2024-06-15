package com.john.backend_gestion_restaurantes.servicios.reserva;

import java.util.List;
import java.util.Optional;

import com.john.backend_gestion_restaurantes.dto.ReservaDTO;
import com.john.backend_gestion_restaurantes.modelos.Reserva;

public interface ReservaService {
    
    List<ReservaDTO> findAllReservas();
    Optional<ReservaDTO> findById(Integer id);
    Optional<Reserva> findReservaById(Integer id);
    Optional<ReservaDTO> saveReserva(Reserva reserva);
    void deleteReservaById(Integer id);
    void deleteAll();
    List<ReservaDTO> getReservasCreatedByUser(String userId);

    
}
