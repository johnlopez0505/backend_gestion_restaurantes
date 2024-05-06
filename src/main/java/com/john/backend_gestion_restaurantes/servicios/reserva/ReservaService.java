package com.john.backend_gestion_restaurantes.servicios.reserva;

import java.util.List;
import java.util.Optional;

import com.john.backend_gestion_restaurantes.modelos.Reserva;

public interface ReservaService {
    
    List<Reserva> findAllReservas();
    Optional<Reserva> findReservaById(Integer id);
    Reserva saveReserva(Reserva reserva);
    void deleteReservaById(Integer id);
}
