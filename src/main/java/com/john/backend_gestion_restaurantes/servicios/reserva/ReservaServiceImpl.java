package com.john.backend_gestion_restaurantes.servicios.reserva;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.john.backend_gestion_restaurantes.modelos.Reserva;
import com.john.backend_gestion_restaurantes.repositorios.RepoReservas;

@Service
public class ReservaServiceImpl implements ReservaService{

    private RepoReservas repoReservas;

    public ReservaServiceImpl(RepoReservas repoReservas){
        this.repoReservas = repoReservas;
    }

    @Override
    public List<Reserva> findAllReservas() {
      return repoReservas.findAll();
    }

    @Override
    public Optional<Reserva> findReservaById(Integer id) {
       return repoReservas.findById(id);
    }

    @Override
    public Reserva saveReserva(Reserva reserva) {
       return repoReservas.save(reserva);
    }

    @Override
    public void deleteReservaById(Integer id) {
       repoReservas.deleteById(id);
    }
}
