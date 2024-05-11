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

import com.john.backend_gestion_restaurantes.modelos.Calificacion;
import com.john.backend_gestion_restaurantes.modelos.Restaurante;
import com.john.backend_gestion_restaurantes.modelos.Usuario;
import com.john.backend_gestion_restaurantes.servicios.calificacion.CalificacionService;
import com.john.backend_gestion_restaurantes.servicios.restaurante.RestauranteService;
import com.john.backend_gestion_restaurantes.servicios.usuarios.AuthService;

@RestController
@RequestMapping("/api")
public class CalificacionController {

    @Autowired
    private CalificacionService calificacionService;
    
    @Autowired
    private RestauranteService restauranteService;

    @Autowired
    private AuthService authService;

    public CalificacionController(
                CalificacionService calificacionService, 
                RestauranteService restauranteService
        ){
        this.calificacionService = calificacionService;
        this.restauranteService = restauranteService;
    }

    @GetMapping("/calificaciones")
    public ResponseEntity<Object> findAll(){
        //System.out.println("ingresa en buscar todos las calificaciones ");
        try {
            List<Calificacion> calificaciones = calificacionService.findAllCalificacion();
            Map<String, Object> response = new HashMap<>();
            if (!calificaciones.isEmpty()) {
                response.put("result", "ok");
                response.put("calificación", calificaciones.stream().map(
                    calificacion -> Map.of(
                                    "id", calificacion.getId(),
                                    "restauranteId", calificacion.getRestaurante().getId(),
                                    "usuarioId", calificacion.getRestaurante().getUsuario().getId(),
                                    "puntuacion", calificacion.getPuntuacion(),
                                    "comentario", calificacion.getComentario()
                                   )
                                )
                            );
                return ResponseEntity.ok(response);
            } else {
                response.put("result", "ok");
                response.put("details", "No se encontro ninguna calificacion en la base de datos");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            // Manejo de la excepción
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("result", "error");
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("message", "Se produjo un error al procesar la solicitud: " + e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("path", "/api/calificaciones");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/calificaciones/{id}")
    public ResponseEntity<Object> findMenuById(@PathVariable Integer id){
        try {
            Optional<Calificacion> calificaciones = calificacionService.findCalificacionById(id);
            Map<String, Object> response = new HashMap<>();
            if (!calificaciones.isPresent()) {
                // Manejo del caso en el que no se encuentra la calificacion
                response.put("result", "error");
                response.put("message", "No se encontró una calificación con el ID proporcionado");
                return ResponseEntity.ok(response);
            }
            Calificacion existingCalificacion = calificaciones.get();
            // Devuelve una respuesta exitosa con la calificación recién creado
            response.put("result", "ok");
            response.put("calificación", Map.of(
                                "id", existingCalificacion.getId(),
                                "restauranteId", existingCalificacion.getRestaurante().getId(),
                                "usuarioId", existingCalificacion.getRestaurante().getUsuario().getId(),
                                "puntuacion", existingCalificacion.getPuntuacion(),
                                "comentario", existingCalificacion.getComentario()
                                )
                            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Manejo de la excepción
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            errorResponse.put("message", "Se produjo un error al buscar la calificación: " + e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("path", "/api/calificaciones/{id}");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    } 

    @PostMapping("/calificaciones/add")
    public ResponseEntity<Object> createMenu(@RequestHeader Integer id,
                                             @RequestBody Calificacion calificacion) {
        try {
            Optional<Restaurante> optionalRestaurante = restauranteService.findById(id);
            Usuario user = authService.getCurrentUser();
            Map<String, Object> response = new HashMap<>();
            if (!optionalRestaurante.isPresent()) {
                // Manejo del caso en el que no se encuentra el restaurante
                response.put("result", "error");
                response.put("message", "No se encontró un restaurante con el ID proporcionado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            Restaurante restaurante = optionalRestaurante.get();
            calificacion.setRestaurante(restaurante);
            calificacion.setUsuario(user);
            // Guarda la nueva calificación en la base de datos
            Calificacion nuevaCalificacion = this.calificacionService.saveCalificacion(calificacion);
            // Devuelve una respuesta exitosa con la calificación recién creada
            response.put("result", "ok");
            response.put("calificación", Map.of("id", nuevaCalificacion.getId(),
                                "restauranteId", nuevaCalificacion.getRestaurante().getId(),
                                "usuarioId", nuevaCalificacion.getRestaurante().getUsuario().getId(),
                                "puntuacion", nuevaCalificacion.getPuntuacion(),
                                "comentario", nuevaCalificacion.getComentario()
                               )
                            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            // Manejo de la excepción
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            errorResponse.put("message", "Se produjo un error al agregar la calificación: " + e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("path", "/api/calificaciones");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/calificaciones/edit/{id}")
    public ResponseEntity<Object> updateMenu(@PathVariable Integer id, @RequestBody Calificacion calificacion) {
        try {
            Optional<Calificacion> optionalCalificacOptional = calificacionService.findCalificacionById(id);
            Map<String, Object> response = new HashMap<>();
            if (!optionalCalificacOptional.isPresent()) {
                // Manejo del caso en el que no se encuentra la calificación
                response.put("result", "error");
                response.put("message", "No se encontró una calificacion con el ID proporcionado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            Calificacion existingCalificacion = optionalCalificacOptional.get();
            existingCalificacion.setPuntuacion(calificacion.getPuntuacion());
            existingCalificacion.setComentario(calificacion.getComentario());
            // Guarda una nueva calificación en la base de datos
            Calificacion nuevaCalificacion = calificacionService.saveCalificacion(existingCalificacion);
            // Devuelve una respuesta exitosa con la calificación recién creada
            response.put("result", "ok");
            response.put("menus", Map.of(
                            "id", nuevaCalificacion.getId(),
                            "restauranteId", nuevaCalificacion.getRestaurante().getId(),
                            "usuarioId", nuevaCalificacion.getRestaurante().getUsuario().getId(),
                            "puntuacion", nuevaCalificacion.getPuntuacion(),
                            "comentario", nuevaCalificacion.getComentario()
                            )
                        );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            // Manejo de la excepción
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            errorResponse.put("message", "Se produjo un error al actualizar la calificación: " + e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("path", "/api/calificaciones/{id}");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PatchMapping("/calificaciones/edit/{id}")
    public ResponseEntity<Object> patchMenu(@PathVariable Integer id, @RequestBody Map<String, Object> updates) {
        try {
            Optional<Calificacion> optionalCalificaciones = calificacionService.findCalificacionById(id);
            //System.out.println("este la calificación consultado por id " + optionalCalificaciones.get().toString());
            Map<String, Object> response = new HashMap<>();
            if (!optionalCalificaciones.isPresent()) {
                // Manejo del caso en el que no se encuentra la calificación
                response.put("result", "error");
                response.put("message", "No se encontró la calificación con el ID proporcionado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            Calificacion existingCalificacion = optionalCalificaciones.get();
            // Actualizar cada campo proporcionado en el objeto JSON de actualizaciones
            updates.forEach((campo, valor) -> {
                switch (campo) {
                    case "puntuacion":
                        existingCalificacion.setPuntuacion(((Integer) valor).intValue());
                        break;
                    case "comentario":
                        existingCalificacion.setComentario(valor.toString());
                        break;
                    default:
                        // Ignora campos desconocidos
                        //response.put(campo, "No se encontró parametro");
                        break;
                }
            });
            Calificacion editCalificacion = calificacionService.saveCalificacion(existingCalificacion);
            // Devuelve una respuesta exitosa con la calificacion recién creada
            response.put("result", "ok");
            response.put("menus", Map.of(
                                "id", editCalificacion.getId(),
                                "restauranteId", editCalificacion.getRestaurante().getId(),
                                "usuarioId", editCalificacion.getRestaurante().getUsuario().getId(),
                                "puntuacion", editCalificacion.getPuntuacion(),
                                "calificacion", editCalificacion.getComentario()
                                )
                            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            // Manejo de la excepción
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            errorResponse.put("message", "Se produjo un error al actualizar la calificación: " + e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("path", "/api/calificacion/{id}");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("/calificaciones/delete/{id}")
    public ResponseEntity<Object> deleteMenu(@PathVariable Integer id) {
        try {
            // Buscar la calificació por su ID
            Optional<Calificacion> optionalCalificacOptional = calificacionService.findCalificacionById(id);
            Map<String, Object> response = new HashMap<>();
            if (optionalCalificacOptional.isPresent()) {
                // Si se encuentra la calificacion, la eliminamos de la base de datos
                calificacionService.deleteCalificacionById(id);
                // Devolver una respuesta indicando que la calificación fue eliminada correctamente
                response.put("result", "ok");
                response.put("message", "La calificacion se eliminó correctamente");
                return ResponseEntity.ok(response);
            } else {
                // Si no se encuentra la calificacion con el ID proporcionado
                // devolvemos una respuesta indicando que no se encontró el menú
                response.put("result", "error");
                response.put("message", "No se encontró una calificación con el ID proporcionado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
             // Manejo de la excepción
             Map<String, Object> errorResponse = new HashMap<>();
             errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
             errorResponse.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
             errorResponse.put("message", "Se produjo un error al eliminar la calificación: " + e.getMessage());
             errorResponse.put("timestamp", LocalDateTime.now());
             errorResponse.put("delete", "/api/calificaciones/{id}");
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }   
    }

    @GetMapping("/calificaciones/usuario")
    public ResponseEntity<List<Calificacion>> getAllCalificacionesCreatedByCurrentUser() {
        // Obtener el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        // Obtener las calificaciones creadas por el usuario actual
        List<Calificacion> calificacionesCreatedByCurrentUser = calificacionService.getCalificacionesCreatedByUser(currentUsername);
        return ResponseEntity.ok(calificacionesCreatedByCurrentUser);
    }
    
}
