package com.john.backend_gestion_restaurantes.controladores;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.john.backend_gestion_restaurantes.modelos.Reserva;
import com.john.backend_gestion_restaurantes.modelos.Restaurante;
import com.john.backend_gestion_restaurantes.servicios.reserva.ReservaService;
import com.john.backend_gestion_restaurantes.servicios.restaurante.RestauranteService;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<Object> findAllReservas() {
        try {
            List<Reserva> reservas = reservaService.findAllReservas();
            Map<String, Object> response = new HashMap<>();
            if (!reservas.isEmpty()) {
                response.put("result", "ok");
                response.put("reservas", reservas.stream().map(
                    reserva -> Map.of("id", reserva.getId(),
                                   "restauranteId", reserva.getRestaurante().getId(),
                                   "usuarioId", reserva.getRestaurante().getUsuario().getId(),
                                   "numeroPersonas", reserva.getNumeroPersonas(),
                                   "fecha", reserva.getFechaYHora()
                                   )
                                )
                            );
                return ResponseEntity.ok(response);
            } else {
                response.put("result", "ok");
                response.put("details", "No se encontro ninguna reserva en la base de datos");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            // Manejo de la excepción
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("result", "error");
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            errorResponse.put("message", "Se produjo un error al procesar la solicitud: " + e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("get", "/api/reservas");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/reservas/{id}")
    public ResponseEntity<Object> findAllReservas(@PathVariable Integer id) {
       try {
            Optional<Reserva> optionalReserva = reservaService.findReservaById(id);
            Map<String, Object> response = new HashMap<>();
            if (!optionalReserva.isPresent()) {
                // Manejo del caso en el que no se encuentra el menú
                response.put("result", "error");
                response.put("message", "No se encontró una reserva con el ID proporcionado");
                return ResponseEntity.ok(response);
            }
            Reserva existingReserva = optionalReserva.get();
            // Devuelve una respuesta exitosa con el menú recién creado
            response.put("result", "ok");
            response.put("menus", Map.of("id", existingReserva.getId(),
                                "restauranteId", existingReserva.getRestaurante().getId(),
                                "usuarioId", existingReserva.getRestaurante().getUsuario().getId(),
                                "numero_personas", existingReserva.getNumeroPersonas(),
                                "fecha", existingReserva.getFechaYHora()
                                )
                            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Manejo de la excepción
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            errorResponse.put("message", "Se produjo un error al buscar la reserva: " + e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("get", "/api/reservas/{id}");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("path")
     public ResponseEntity<Object> createReserva(@RequestHeader Integer id, @RequestBody Reserva reserva) {
        try {
            Optional<Restaurante> optionalRestaurante = restauranteService.findById(id);
            Map<String, Object> response = new HashMap<>();
            if (!optionalRestaurante.isPresent()) {
                // Manejo del caso en el que no se encuentra el restaurante
                response.put("result", "error");
                response.put("message", "No se encontró un restaurante con el ID proporcionado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            Restaurante restaurante = optionalRestaurante.get();
            reserva.setRestaurante(restaurante);
            reserva.setUsuario(restaurante.getUsuario());
            // Guarda el nuevo menú en tu base de datos
            Reserva nuevaReserva = reservaService.saveReserva(reserva);
            // Devuelve una respuesta exitosa con el menú recién creado
            response.put("result", "ok");
            response.put("menus", Map.of("id", nuevaReserva.getId(),
                                "restauranteId", nuevaReserva.getRestaurante().getId(),
                                "usuarioId", nuevaReserva.getRestaurante().getUsuario().getId(),
                                "numero_personas", nuevaReserva.getNumeroPersonas(),
                                "fecha", nuevaReserva.getFechaYHora()
                                )
                            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            // Manejo de la excepción
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            errorResponse.put("message", "Se produjo un error al crear la reserva: " + e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("post", "/api/resrvas{id}");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/reservas/usuario")
    public ResponseEntity<List<Reserva>> getAllReservasCreatedByCurrentUser() {
        // Obtener el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        // Obtener los menús creados por el usuario actual
        List<Reserva> reservasCreatedByCurrentUser = reservaService.getReservasCreatedByUser(currentUsername);
        return ResponseEntity.ok(reservasCreatedByCurrentUser);
    }
    
    
}
