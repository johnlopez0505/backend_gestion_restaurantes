package com.john.backend_gestion_restaurantes.controladores;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.john.backend_gestion_restaurantes.dto.ReservaDTO;
import com.john.backend_gestion_restaurantes.dto.response.ReservaResponse;
import com.john.backend_gestion_restaurantes.modelos.Reserva;
import com.john.backend_gestion_restaurantes.modelos.Restaurante;
import com.john.backend_gestion_restaurantes.modelos.Usuario;
import com.john.backend_gestion_restaurantes.servicios.reserva.ReservaService;
import com.john.backend_gestion_restaurantes.servicios.restaurante.RestauranteService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@RequestMapping("/api")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private RestauranteService restauranteService;

    @GetMapping("/reservas")
    public ResponseEntity<ReservaResponse<List<ReservaDTO>>> obtenerTodasLasReservas() {
        try {
            List<ReservaDTO> reservas = reservaService.findAllReservas();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                ReservaResponse<List<ReservaDTO>> response = new ReservaResponse<>(
                    "error", "No autorizado", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            if (reservas == null || reservas.isEmpty()) {
                ReservaResponse<List<ReservaDTO>> response = new ReservaResponse<>(
                    "error", "No hay reservas almacenadas en la base de datos", reservas);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            ReservaResponse<List<ReservaDTO>> response = new ReservaResponse<>(
                "ok", "Reservas listadas con éxito", reservas);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ReservaResponse<List<ReservaDTO>> response = new ReservaResponse<>(
                "error", "Ocurrió un error al obtener las reservas", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/reservas/{id}")
    public ResponseEntity<ReservaResponse<ReservaDTO>> obtenerReservaPorId(@PathVariable Integer id) {
        try {
            Optional<ReservaDTO> reserva = reservaService.findById(id);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                ReservaResponse<ReservaDTO> response = new ReservaResponse<>(
                    "error", "No autorizado", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            if (!reserva.isPresent()) {
                ReservaResponse<ReservaDTO> response = new ReservaResponse<>(
                    "error", "No se encontró una reserva con el ID proporcionado", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            ReservaResponse<ReservaDTO> response = new ReservaResponse<>(
                "ok", "Reserva listada con éxito", reserva.get());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ReservaResponse<ReservaDTO> response = new ReservaResponse<>(
                "error", "Ocurrió un error al obtener la reserva", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/reservas/add")
    public ResponseEntity<ReservaResponse<ReservaDTO>> crearReserva(@RequestHeader Integer id, 
                                                @RequestBody  Reserva reserva) {
        try {
            // Obtener el usuario autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                ReservaResponse<ReservaDTO> response = new ReservaResponse<>(
                    "error", "El usuario no esta autorizado", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
          
            Optional<Restaurante> optionalRestaurante = restauranteService.findRestauranteById(id);

            if (!optionalRestaurante.isPresent()) {
                ReservaResponse<ReservaDTO> response = new ReservaResponse<>(
                    "error", "No se encontró un restaurante con el ID proporcionado", null);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            Restaurante existingRestaurante = optionalRestaurante.get();
            reserva.setRestaurante(existingRestaurante);
            reserva.setUsuario(existingRestaurante.getUsuario());

            // Validar que la fecha y hora no sea inferior a la fecha y hora actual
            if (reserva.getFechaYHora().isBefore(LocalDateTime.now())) {
                ReservaResponse<ReservaDTO> response = new ReservaResponse<>(
                    "error", "La fecha y hora de la reserva no puede ser anterior a la fecha y hora actual", null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            Optional<ReservaDTO> nuevaReserva = reservaService.saveReserva(reserva);
            ReservaResponse<ReservaDTO> response = new ReservaResponse<>(
                "ok", "Reserva creada con éxito", nuevaReserva.get());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ReservaResponse<ReservaDTO> response = new ReservaResponse<>(
                "error", "Ocurrió un error al crear la reserva: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/reservas/edit/{id}")
    public ResponseEntity<ReservaResponse<ReservaDTO>> actualizarReserva(@PathVariable Integer id, 
                                @RequestBody Reserva reserva) {
        try {
            Optional<Reserva> optionalReserva = reservaService.findReservaById(id);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                ReservaResponse<ReservaDTO> response = new ReservaResponse<>(
                    "error", "El usuario no está autorizado", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            if (!optionalReserva.isPresent()) {
                ReservaResponse<ReservaDTO> response = new ReservaResponse<>(
                    "error", "No se encontró una reserva con el ID proporcionado", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            Reserva existingReserva = optionalReserva.get();

            // Validar que la fecha y hora no sea inferior a la fecha y hora actual
            if (reserva.getFechaYHora().isBefore(LocalDateTime.now())) {
                ReservaResponse<ReservaDTO> response = new ReservaResponse<>(
                    "error", "La fecha y hora de la reserva no puede ser anterior a la fecha y hora actual", null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Actualizar los campos de la reserva
            existingReserva.setNombreCliente(reserva.getNombreCliente());
            existingReserva.setTelefonoCliente(reserva.getTelefonoCliente());
            existingReserva.setEmailCliente(reserva.getEmailCliente());
            existingReserva.setNumeroPersonas(reserva.getNumeroPersonas());
            existingReserva.setNotas(reserva.getNotas());
            existingReserva.setFechaYHora(reserva.getFechaYHora());

            Optional<ReservaDTO> nuevaReserva = reservaService.saveReserva(existingReserva);
            ReservaResponse<ReservaDTO> response = new ReservaResponse<>(
                "ok", "Reserva actualizada con éxito", nuevaReserva.get());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ReservaResponse<ReservaDTO> response = new ReservaResponse<>(
                "error", "Ocurrió un error al actualizar la reserva: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PatchMapping("/reservas/edit/{id}")
    public ResponseEntity<ReservaResponse<ReservaDTO>> actualizarReservaPatch(@PathVariable Integer id, 
                                            @RequestBody  Map<String, Object> updates) {
        try {
            Optional<Reserva> optionalReserva = reservaService.findReservaById(id);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                ReservaResponse<ReservaDTO> response = new ReservaResponse<>(
                    "error", "El usuario no está autorizado", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            if (!optionalReserva.isPresent()) {
                ReservaResponse<ReservaDTO> response = new ReservaResponse<>(
                    "error", "No se encontró una reserva con el ID proporcionado", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            Reserva existingReserva = optionalReserva.get();
            // Actualizar cada campo proporcionado en el objeto JSON de actualizaciones
            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                String campo = entry.getKey();
                Object valor = entry.getValue();
                switch (campo) {
                    case "nombreCliente":
                        if (valor.toString() != null || ! valor.toString().isEmpty()) {
                            existingReserva.setNombreCliente((String) valor);
                        }
                        break;
                    case "telefonoCliente":
                        if (valor.toString() != null || ! valor.toString().isEmpty()) {
                            existingReserva.setTelefonoCliente((String) valor);
                        }
                        break;
                    case "emailCliente":
                        if (valor.toString() != null || ! valor.toString().isEmpty()) {
                            existingReserva.setEmailCliente((String) valor);
                        }
                        break;
                    case "numeroPersonas":
                        if (valor.toString() != null || ! valor.toString().isEmpty()) {
                            existingReserva.setNumeroPersonas((Integer) valor);
                        }
                        break;
                    case "notas":
                        if (valor.toString() != null || ! valor.toString().isEmpty()) {
                            existingReserva.setNotas((String) valor);
                        }
                        break;
                    case "fechaYHora":
                        if (valor != null && !valor.toString().isEmpty()) {
                            String fechaYHoraStr = valor.toString();
                            DateTimeFormatter formatter;
                            if (fechaYHoraStr.contains("/")) {
                                formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                            } else if (fechaYHoraStr.contains("-")) {
                                formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                            } else {
                                ReservaResponse<ReservaDTO> errorResponse = new ReservaResponse<>(
                                    "error", "No se puede determinar el formato de fecha y hora", null);
                                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
                            }
                            LocalDateTime nuevaFechaYHora = LocalDateTime.parse(fechaYHoraStr, formatter);
                            // Comprobar si la nueva fecha y hora es posterior a la fecha y hora actual
                            if (nuevaFechaYHora.isBefore(LocalDateTime.now())) {
                                ReservaResponse<ReservaDTO> errorResponse = new ReservaResponse<>(
                                    "error", "La nueva fecha y hora no puede ser anterior a la fecha y hora actual", null);
                                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
                            }
                            existingReserva.setFechaYHora(nuevaFechaYHora);
                        }
                        break;
                    default:
                        // Campos desconocidos
                        ReservaResponse<ReservaDTO> errorResponse = new ReservaResponse<>(
                            "error", "No se encontró parámetro: " + campo, null);
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
                }
            }
            // Guardar la reserva actualizada en la base de datos
            Optional<ReservaDTO> nuevaReserva = reservaService.saveReserva(existingReserva);
            ReservaResponse<ReservaDTO> response = new ReservaResponse<>(
                "ok", "Reserva actualizada con éxito", nuevaReserva.get());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ReservaResponse<ReservaDTO> response = new ReservaResponse<>(
                "error", "Ocurrió un error al actualizar la reserva: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @DeleteMapping("/reservas/delete/{id}")
    public ResponseEntity<ReservaResponse<ReservaDTO>> eliminarReserva(@PathVariable Integer id) {
        try {
            reservaService.deleteReservaById(id);
            ReservaResponse<ReservaDTO> response = new ReservaResponse<>(
                "ok", "Reserva eliminada con éxito", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ReservaResponse<ReservaDTO> response = new ReservaResponse<>(
                "error", "Ocurrió un error al eliminar la reserva", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/reservas/usuario")
    public ResponseEntity<ReservaResponse<List<ReservaDTO>>> obtenerReservasCreadasPorUsuarioActual() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                ReservaResponse<List<ReservaDTO>> response = new ReservaResponse<>(
                    "error", "No autorizado", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            // Obtener el ID del usuario autenticado
            String currentUserId = String.valueOf(((Usuario) authentication.getPrincipal()).getId());
            List<ReservaDTO> reservasCreadasPorUsuario = reservaService.getReservasCreatedByUser(currentUserId);
            if (reservasCreadasPorUsuario == null || reservasCreadasPorUsuario.isEmpty()) {
                ReservaResponse<List<ReservaDTO>> response = new ReservaResponse<>(
                    "error", "El usuario no tiene reservas en la base de datos", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            ReservaResponse<List<ReservaDTO>> response = new ReservaResponse<>(
                "ok", "Reservas listadas con éxito", reservasCreadasPorUsuario);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ReservaResponse<List<ReservaDTO>> response = new ReservaResponse<>(
                "error", "Ocurrió un error al obtener las reservas del usuario", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
