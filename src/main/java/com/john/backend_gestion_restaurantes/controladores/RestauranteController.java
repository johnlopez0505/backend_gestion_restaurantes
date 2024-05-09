package com.john.backend_gestion_restaurantes.controladores;

import com.john.backend_gestion_restaurantes.modelos.Restaurante;
import com.john.backend_gestion_restaurantes.modelos.Usuario;
import com.john.backend_gestion_restaurantes.servicios.restaurante.RestauranteService;
import com.john.backend_gestion_restaurantes.servicios.usuarios.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class RestauranteController {

    @Autowired
    private RestauranteService restauranteService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/restaurantes")
    public ResponseEntity<Object> obtenerTodosLosRestaurantes(){
        try {
            List<Restaurante> restaurantes = this.restauranteService.findAllRestaurantes();
            Map<String, Object> response = new HashMap<>();
            if (!restaurantes.isEmpty()) {
                response.put("result", "ok");
                response.put("restaurantes", restaurantes.stream().map(
                   restaurante -> Map.of("id",restaurante.getId(),
                                   "usuarioId",restaurante.getUsuario().getId(),
                                   "nombre",restaurante.getNombre(),
                                   "ciudad",restaurante.getCiudad(),
                                   "provincia",restaurante.getProvincia(),
                                   "telefono", restaurante.getTelefono(),
                                   "imagen",restaurante.getImagen() != null ?restaurante.getImagen() : "Sin imagen"
                                   )
                                )
                            );
                return ResponseEntity.ok(response);
            } else {
                response.put("result", "ok");
                response.put("message", "No se encontro ningun restaurante en la base de datos");
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
            errorResponse.put("get", "/api/restaurantes");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/restaurantes/{id}")
    public ResponseEntity<Object> obtenerRestaurantePorId(@PathVariable Integer id) {
       try {
        Optional<Restaurante> optionalRestaurante = restauranteService.findById(id);
        Map<String, Object> response = new HashMap<>();
        if(optionalRestaurante.isPresent()) {
            Restaurante restaurante = optionalRestaurante.get();
            response.put("result", "ok");
            response.put("restaurantes", Map.of("id",restaurante.getId(),
                               "usuarioId",restaurante.getUsuario().getId(),
                               "nombre",restaurante.getNombre(),
                               "ciudad",restaurante.getCiudad(),
                               "provincia",restaurante.getProvincia(),
                               "telefono", restaurante.getTelefono(),
                               "imagen",restaurante.getImagen() != null ?restaurante.getImagen() : "Sin imagen"
                            )
                        );
            return ResponseEntity.ok(response);
        }else{
            // Manejo del caso en el que no se encuentra el restaurante
            response.put("result", "error");
            response.put("message", "No se encontró un restaurante con el ID proporcionado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
       } catch (Exception e) {
         // Manejo de la excepción
         Map<String, Object> errorResponse = new HashMap<>();
         errorResponse.put("result", "error");
         errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
         errorResponse.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
         errorResponse.put("message", "Se produjo un error al procesar la solicitud: " + e.getMessage());
         errorResponse.put("timestamp", LocalDateTime.now());
         errorResponse.put("get", "/api/restaurantes/{id}");
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
       }
    }

    @PostMapping("/restaurantes")
    public ResponseEntity<Object> createMenu(@RequestHeader Integer usuarioId, @RequestBody Restaurante restaurante) {
        try {
            Optional<Usuario> optionalUsuario = usuarioService.findUsuarioById(usuarioId);
            Map<String, Object> response = new HashMap<>();
            if (!optionalUsuario.isPresent()) {
                // Manejo del caso en el que no se encuentre el usuario
                response.put("result", "error");
                response.put("message", "Usuario no encontrado con el id proporcionado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            Usuario usuario = optionalUsuario.get();
            restaurante.setUsuario(usuario);
            // Guarda el nuevo restaurante en la base de datos
            Restaurante nuevoRestaurante = restauranteService.save(restaurante);
            // Devuelve una respuesta exitosa con el restaurante recién creado
            response.put("result", "ok");
            response.put("restaurantes", Map.of("id", nuevoRestaurante.getId(),
                                "usuarioId", nuevoRestaurante.getUsuario().getId(),
                                "nombre", nuevoRestaurante.getNombre(),
                                "ciudad", nuevoRestaurante.getCiudad(),
                                "provincia", nuevoRestaurante.getProvincia(),
                                "telefono", nuevoRestaurante.getTelefono(),
                                "imagenPlato", nuevoRestaurante.getImagen()
                                )
                            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            // Manejo de la excepción
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            errorResponse.put("message", "Se produjo un error al agregar el restaurante: " + e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("post", "/api/menus");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    

    @PutMapping("/restaurantes")
    public ResponseEntity<Object> actualizarRestaurante(@RequestHeader  Integer id, @RequestBody Restaurante restaurante){
        try {
            Optional<Restaurante> optionalRestaurante = restauranteService.findById(id);
            Map<String,Object> response = new HashMap<>();
            if (!optionalRestaurante.isPresent()) {
                // Manejo del caso en el que no se encuentra el restaurante
                response.put("result", "error");
                response.put("message", "Restaurante no encontrado con el id proporcionado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Restaurante existendRestaurante = optionalRestaurante.get();
            existendRestaurante.setNombre(restaurante.getNombre());
            existendRestaurante.setCiudad(restaurante.getCiudad());
            existendRestaurante.setProvincia(restaurante.getProvincia());
            existendRestaurante.setTelefono(restaurante.getTelefono());
            existendRestaurante.setImagen(restaurante.getImagen());
            Restaurante editRestaurante = restauranteService.save(existendRestaurante);
            response.put("result",  "ok");
            response.put("restaurantes", Map.of("id",editRestaurante.getId(),
                                "usuarioId", editRestaurante.getUsuario().getId(),
                                "nombre", editRestaurante.getNombre(),
                                "ciudad", editRestaurante.getCiudad(),
                                "provincia", editRestaurante.getProvincia(),
                                "telefono", editRestaurante.getTelefono(),
                                "imagen", editRestaurante.getImagen()
                            )
                        );
            return ResponseEntity.ok(response);
          
        } catch (Exception e) {
             // Manejo de la excepción
             Map<String, Object> errorResponse = new HashMap<>();
             errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
             errorResponse.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
             errorResponse.put("message", "Se produjo un error al actualizar el restaurante: " + e.getMessage());
             errorResponse.put("timestamp", LocalDateTime.now());
             errorResponse.put("put", "/api/menus");
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PatchMapping("/restaurantes")
    public ResponseEntity<Object> actualizarRestaurantePatch(@RequestHeader Integer id,  @RequestBody Map<String, Object> updates) {
        try {
            Optional<Restaurante> optionalRestaurante = restauranteService.findById(id);
            //System.out.println("este es el menu consultado por id " + optionalMenu.get().toString());
            Map<String, Object> response = new HashMap<>();
            if (!optionalRestaurante.isPresent()) {
                // Manejo del caso en el que no se encuentra el restaurante
                response.put("result", "error");
                response.put("message", "No se encontró un restaurante con el ID proporcionado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            Restaurante restauranteExistente = optionalRestaurante.get();
            // Actualizar cada campo proporcionado en el objeto JSON de actualizaciones
            updates.forEach((campo, valor) -> {
                switch (campo) {
                    case "nombre":
                       restauranteExistente.setNombre(valor.toString());
                        break;
                    case "ciudad":
                       restauranteExistente.setCiudad(valor.toString());
                        break;
                    case "provincia":
                       restauranteExistente.setProvincia(valor.toString());
                        break;
                    case "telefono":
                        restauranteExistente.setTelefono(valor.toString());
                    break;
                    case "imagen":
                       restauranteExistente.setImagen(valor.toString());
                        break;
                    default:
                        // Ignora campos desconocidos
                        //response.put(campo, "No se encontró parametro");
                        break;
                }
            });
            Restaurante nuevoRestaurante = restauranteService.save(restauranteExistente);
            // Devuelve una respuesta exitosa con el menú recién creado
            response.put("result", "ok");
            response.put("restaurantes", Map.of("id",nuevoRestaurante.getId(),
                            "usuarioId", nuevoRestaurante.getUsuario().getId(),
                            "nombre",nuevoRestaurante.getNombre(),
                            "ciudad",nuevoRestaurante.getCiudad(),
                            "provincia",nuevoRestaurante.getProvincia(),
                            "telefono", nuevoRestaurante.getTelefono(),
                            "imagen",nuevoRestaurante.getImagen()
                            )
                        );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Manejo de la excepción
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            errorResponse.put("message", "Se produjo un error al actualizar el restaurante: " + e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("path", "/api/menus");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("/restaurantes/{id}")
    public ResponseEntity<Object> deleteMenu(@PathVariable Integer id) {
        try {
            // Buscar el restaurante por su ID
            Optional<Restaurante> restauranteOptional = restauranteService.findById(id);
            Map<String, Object> response = new HashMap<>();
            if (restauranteOptional.isPresent()) {
                Restaurante restaurante = restauranteOptional.get();
                // Si se encuentra el restaurante, eliminarlo de la base de datos
                restauranteService.deleteById(restaurante.getId().intValue());
                // Devolver una respuesta indicando que el restaurante fue eliminado correctamente
                response.put("result", "ok");
                response.put("message", "El restaurante se eliminó correctamente");
                return ResponseEntity.ok(response);
            } else {
                // Si no se encuentra el restaurante con el ID proporcionado, 
                // devolver una respuesta indicando que no se encontró el restaurante
                response.put("result", "error");
                response.put("message", "No se encontró un restaurante con el ID proporcionado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
             // Manejo de la excepción
             Map<String, Object> errorResponse = new HashMap<>();
             errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
             errorResponse.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
             errorResponse.put("message", "Se produjo un error al eliminar el restaurante: " + e.getMessage());
             errorResponse.put("timestamp", LocalDateTime.now());
             errorResponse.put("delete", "/api/menus/{id}");
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
