package com.john.backend_gestion_restaurantes.controladores;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.john.backend_gestion_restaurantes.dto.CalificacionDTO;
import com.john.backend_gestion_restaurantes.dto.response.CalificacionResponse;
import com.john.backend_gestion_restaurantes.modelos.Calificacion;
import com.john.backend_gestion_restaurantes.modelos.Restaurante;
import com.john.backend_gestion_restaurantes.modelos.Usuario;
import com.john.backend_gestion_restaurantes.servicios.calificacion.CalificacionService;
import com.john.backend_gestion_restaurantes.servicios.restaurante.RestauranteService;


@RestController
@RequestMapping("/api")
public class CalificacionController {

    @Autowired
    private CalificacionService calificacionService;
    
    @Autowired
    private RestauranteService restauranteService;
    

    @GetMapping("/calificaciones")
     public ResponseEntity<CalificacionResponse<List<CalificacionDTO>>> obtenerTodasLasCalificaciones() {
        try {
            System.out.println("entramos al get");
            List<CalificacionDTO> calificaciones = calificacionService.findAllCalificacion();
            System.out.println("estas son las calificaciones del get: "+calificaciones);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                CalificacionResponse<List<CalificacionDTO>> response = new CalificacionResponse<>(
                    "error", "No autorizado", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            if (calificaciones == null || calificaciones.isEmpty()) {
                CalificacionResponse<List<CalificacionDTO>> response = new CalificacionResponse<>(
                    "error", "No hay calificaciones almacenadas en la base de datos", calificaciones);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            CalificacionResponse<List<CalificacionDTO>> response = new CalificacionResponse<>(
                "ok", "Calificaciones listadas con éxito", calificaciones);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            CalificacionResponse<List<CalificacionDTO>> response = new CalificacionResponse<>(
                "error", "Ocurrió un error al obtener las calificaciones", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
   

    @GetMapping("/calificaciones/{id}")
    public ResponseEntity<CalificacionResponse<CalificacionDTO>> obtenerCalificacionPorId(@PathVariable Integer id) {
        try {
            Optional<CalificacionDTO> calificacion = calificacionService.findById(id);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                CalificacionResponse<CalificacionDTO> response = new CalificacionResponse<>(
                    "error", "No autorizado", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            if (!calificacion.isPresent()) {
                CalificacionResponse<CalificacionDTO> response = new CalificacionResponse<>(
                    "error", "No se encontró una calificación con el ID proporcionado", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            CalificacionResponse<CalificacionDTO> response = new CalificacionResponse<>(
                "ok", "Calificación listada con éxito", calificacion.get());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            CalificacionResponse<CalificacionDTO> response = new CalificacionResponse<>(
                "error", "Ocurrió un error al obtener la calificación", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
   
    @PostMapping("/calificaciones/add")
    public ResponseEntity<CalificacionResponse<CalificacionDTO>> crearCalificacion(@RequestHeader Integer id,
                                                                @RequestBody Calificacion calificacion) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                CalificacionResponse<CalificacionDTO> response = new CalificacionResponse<>(
                    "error", "El usuario no está autorizado", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            Optional<Restaurante> optionalRestaurante = restauranteService.findRestauranteById(id);

            if (!optionalRestaurante.isPresent()) {
                CalificacionResponse<CalificacionDTO> response = new CalificacionResponse<>(
                    "error", "No se encontró un restaurante con el ID proporcionado", null);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Restaurante existingRestaurante = optionalRestaurante.get();
            calificacion.setUsuario(existingRestaurante.getUsuario());
            calificacion.setRestaurante(existingRestaurante);
        
            Optional<CalificacionDTO> nuevaCalificacion = calificacionService.saveCalificacion(calificacion);
            CalificacionResponse<CalificacionDTO> response = new CalificacionResponse<>(
                "ok", "Calificación creada con éxito", nuevaCalificacion.get());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            CalificacionResponse<CalificacionDTO> response = new CalificacionResponse<>(
                "error", "Ocurrió un error al crear la calificación: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/calificaciones/edit/{id}")
    public ResponseEntity<CalificacionResponse<CalificacionDTO>> actualizarCalificacion(@PathVariable Integer id, 
                                                            @RequestBody Calificacion calificacion) {                                                        
        try {
            Optional<Calificacion> optionalCalificacion = calificacionService.findCalificacionById(id);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                CalificacionResponse<CalificacionDTO> response = new CalificacionResponse<>(
                "error", "El usuario no está autorizado", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            if (!optionalCalificacion.isPresent()) {
                CalificacionResponse<CalificacionDTO> response = new CalificacionResponse<>(
                "error", "No se encontró una calificación con el ID proporcionado", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            Calificacion existingCalificacion = optionalCalificacion.get();

            // Actualizar los campos de la calificación
            existingCalificacion.setPuntuacion(calificacion.getPuntuacion());
            existingCalificacion.setComentario(calificacion.getComentario());

            Optional<CalificacionDTO> nuevaCalificacion = calificacionService.saveCalificacion(existingCalificacion);
            CalificacionResponse<CalificacionDTO> response = new CalificacionResponse<>(
            "ok", "Calificación actualizada con éxito", nuevaCalificacion.get());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            CalificacionResponse<CalificacionDTO> response = new CalificacionResponse<>(
            "error", "Ocurrió un error al actualizar la calificación: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @PatchMapping("/calificaciones/edit/{id}")
    public ResponseEntity<CalificacionResponse<CalificacionDTO>> actualizarCalificacionPatch(@PathVariable Integer id, 
                                            @RequestBody  Map<String, Object> updates) {
        try {
            Optional<Calificacion> optionalCalificacion = calificacionService.findCalificacionById(id);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                CalificacionResponse<CalificacionDTO> response = new CalificacionResponse<>(
                    "error", "El usuario no está autorizado", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            if (!optionalCalificacion.isPresent()) {
                CalificacionResponse<CalificacionDTO> response = new CalificacionResponse<>(
                    "error", "No se encontró una calificación con el ID proporcionado", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            Calificacion existingCalificacion = optionalCalificacion.get();
            
            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                String campo = entry.getKey();
                Object valor = entry.getValue();
                switch (campo) {
                    case "puntuacion":
                        if (valor != null) {
                            existingCalificacion.setPuntuacion((Double) valor);
                        }
                        break;
                    case "comentario":
                        if (valor != null && !valor.toString().isEmpty()) {
                            existingCalificacion.setComentario((String) valor);
                        }
                        break;
                    default:
                        CalificacionResponse<CalificacionDTO> errorResponse = new CalificacionResponse<>(
                            "error", "No se encontró parámetro: " + campo, null);
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
                }
            }

            Optional<CalificacionDTO> nuevaCalificacion = calificacionService.saveCalificacion(existingCalificacion);
            CalificacionResponse<CalificacionDTO> response = new CalificacionResponse<>(
                "ok", "Calificación actualizada con éxito", nuevaCalificacion.get());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            CalificacionResponse<CalificacionDTO> response = new CalificacionResponse<>(
                "error", "Ocurrió un error al actualizar la calificación: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @DeleteMapping("/calificaciones/delete/{id}")
    public ResponseEntity<CalificacionResponse<CalificacionDTO>> eliminarCalificacion(@PathVariable Integer id) {
        try {
            calificacionService.deleteCalificacionById(id);
            CalificacionResponse<CalificacionDTO> response = new CalificacionResponse<>(
                "ok", "Calificación eliminada con éxito", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            CalificacionResponse<CalificacionDTO> response = new CalificacionResponse<>(
                "error", "Ocurrió un error al eliminar la calificación", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/calificaciones/usuario")
    public ResponseEntity<CalificacionResponse<List<CalificacionDTO>>> obtenerCalificacionesCreadasPorUsuarioActual() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                CalificacionResponse<List<CalificacionDTO>> response = new CalificacionResponse<>(
                    "error", "No autorizado", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            // Obtener el ID del usuario autenticado
            String currentUserId = String.valueOf(((Usuario) authentication.getPrincipal()).getId());
            List<CalificacionDTO> calificacionesCreadasPorUsuario = calificacionService.getCalificacionesCreatedByUser(currentUserId);
            if (calificacionesCreadasPorUsuario == null || calificacionesCreadasPorUsuario.isEmpty()) {
                CalificacionResponse<List<CalificacionDTO>> response = new CalificacionResponse<>(
                    "error", "El usuario no tiene calificaciones en la base de datos", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            CalificacionResponse<List<CalificacionDTO>> response = new CalificacionResponse<>(
                "ok", "Calificaciones listadas con éxito", calificacionesCreadasPorUsuario);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            CalificacionResponse<List<CalificacionDTO>> response = new CalificacionResponse<>(
                "error", "Ocurrió un error al obtener las calificaciones del usuario", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
}
