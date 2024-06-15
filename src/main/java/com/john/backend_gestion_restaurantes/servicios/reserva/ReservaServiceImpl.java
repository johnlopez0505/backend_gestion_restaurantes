package com.john.backend_gestion_restaurantes.servicios.reserva;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.john.backend_gestion_restaurantes.dto.ReservaDTO;
import com.john.backend_gestion_restaurantes.modelos.Reserva;
import com.john.backend_gestion_restaurantes.repositorios.RepoReservas;


@Service
public class ReservaServiceImpl implements ReservaService{

    private RepoReservas repoReservas;

    public ReservaServiceImpl(RepoReservas repoReservas){
        this.repoReservas = repoReservas;
    }

   @Override
   public List<ReservaDTO> findAllReservas() {
      List<Reserva> reservas = repoReservas.findAll();
        return reservas.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
   }

   @Override
   public Optional<ReservaDTO> findById(Integer id) {
       Optional<Reserva> reserva = repoReservas.findById(id);
        return reserva.map(this::convertToDTO);
   }

   @Override
   public Optional<Reserva> findReservaById(Integer id) {
      return repoReservas.findById(id);
   }

   @Override
   public Optional<ReservaDTO> saveReserva(Reserva reserva) {
      try {
         Reserva savedReserva = repoReservas.save(reserva);
         return Optional.ofNullable(convertToDTO(savedReserva));
      } catch (Exception e) {
         throw new RuntimeException(e.getMessage());
      }
   
      
   }

   @Override
   public void deleteReservaById(Integer id) {
      this.repoReservas.deleteById(id);
   }

   @Override
   public void deleteAll() {
      this.repoReservas.deleteAll();
   }

   @Override
   public List<ReservaDTO> getReservasCreatedByUser(String userId) {
      List<Reserva> reservas = repoReservas.findByCreatedBy(userId);
        return reservas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
   }

   private ReservaDTO convertToDTO(Reserva reserva) {
      
         ReservaDTO dto = new ReservaDTO();
         dto.setId(reserva.getId());
         dto.setRestauranteId(reserva.getRestaurante().getId());
         dto.setUsuarioId(reserva.getUsuario().getId());
         dto.setNombreCliente(reserva.getNombreCliente());
         dto.setTelefonoCliente(reserva.getTelefonoCliente());
         dto.setEmailCliente(reserva.getEmailCliente());
         dto.setNumeroPersonas(reserva.getNumeroPersonas());
         dto.setNotas(reserva.getNotas());
         // Convertir la fecha y hora a una cadena en el formato deseado
         String fechaYHoraStr = reserva.getFechaYHora()
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
         dto.setFechaYHora(fechaYHoraStr);
        return dto;
    }

   
}
