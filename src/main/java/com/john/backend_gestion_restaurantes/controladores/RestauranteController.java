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

import com.john.backend_gestion_restaurantes.dto.RestauranteDTO;
import com.john.backend_gestion_restaurantes.dto.response.RestauranteResponse;
import com.john.backend_gestion_restaurantes.modelos.Restaurante;
import com.john.backend_gestion_restaurantes.modelos.Usuario;
import com.john.backend_gestion_restaurantes.servicios.imagenes.FirebaseStorageService;
import com.john.backend_gestion_restaurantes.servicios.restaurante.RestauranteService;


@RestController
@RequestMapping("/api")
public class RestauranteController {

    @Autowired
    private RestauranteService restauranteService;

    @Autowired
    private FirebaseStorageService firebaseStorageService;



    @GetMapping("/restaurantes")
    public ResponseEntity<RestauranteResponse<List<RestauranteDTO>>> obtenerTodosLosRestaurantes(){
        try {
            List<RestauranteDTO> restaurantes = restauranteService.findAllRestaurantes();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                RestauranteResponse<List<RestauranteDTO>> response = new RestauranteResponse<>(
                    "error", "No autorizado", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            if (restaurantes == null || restaurantes.isEmpty()) {
               RestauranteResponse<List<RestauranteDTO>> response = new RestauranteResponse<>(
                "error", "No hay restaurantes almacenados en la base de datos", restaurantes);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            RestauranteResponse<List<RestauranteDTO>> response = new RestauranteResponse<>(
            "ok", "Restaurantes listados con éxito", restaurantes);
            return ResponseEntity.ok(response);
        
        } catch (Exception e) {
            RestauranteResponse<List<RestauranteDTO>> response = new RestauranteResponse<>(
            "error", "Ocurrió un error al listar los restaurante", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/restaurantes/{id}")
    public ResponseEntity<RestauranteResponse<RestauranteDTO>> obtenerRestaurantePorId(
                                                                @PathVariable Integer id) {
       try {
            Optional<RestauranteDTO> restaurante = restauranteService.findById(id);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                RestauranteResponse<RestauranteDTO> response = new RestauranteResponse<>(
                    "error", "El usuario no esta autorizado", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            if (!restaurante.isPresent()) {
            RestauranteResponse<RestauranteDTO> response = new RestauranteResponse<>(
                "error", "No se encontró un restaurante con el ID proporcionado", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            RestauranteResponse<RestauranteDTO> response = new RestauranteResponse<>(
            "ok", "Restaurante listado con éxito", restaurante.get());
            return ResponseEntity.ok(response);
       } catch (Exception e) {
            RestauranteResponse<RestauranteDTO> response = new RestauranteResponse<>(
            "error", "Ocurrió un error al guardar el restaurante", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
       }
    }

    @PostMapping("/restaurantes/add")
    public ResponseEntity< RestauranteResponse<RestauranteDTO>> createRestaurante(@RequestHeader Integer id, 
                                                    @RequestBody Restaurante restaurante) {
        try {
            // Obtener el usuario autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                RestauranteResponse<RestauranteDTO> response = new RestauranteResponse<>(
                    "error", "El usuario no esta autorizado", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            Usuario  user = (Usuario) authentication.getPrincipal();
            System.out.println("este es el usuario " + user);
            restaurante.setUsuario(user);

            // Guardar la nueva imagen si existe
            if (restaurante.getImagen() != null && !restaurante.getImagen().isEmpty()) {
                String newFileName;
                try {
                    newFileName = firebaseStorageService.uploadBase64Image(restaurante.getImagen());
                    } catch (Exception e) {
                        // Manejo de la excepción
                        throw new RuntimeException("Error al subir la imagen a Firebase", e);
                    }
                    restaurante.setImagen(newFileName);
            } else {
                restaurante.setImagen("ee563e42-e7c3-4734-8077-b99ad71dc145.jpeg");
            }

            Optional<RestauranteDTO> nuevoRestaurante = restauranteService.save(restaurante);
            RestauranteResponse<RestauranteDTO> response = new RestauranteResponse<>(
                "ok", "Restaurante creado con éxito", nuevoRestaurante.get());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            RestauranteResponse<RestauranteDTO> response = new RestauranteResponse<>(
            "error", "Ocurrió un error al guardar el restaurante: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
       }
    }
    

    @PutMapping("/restaurantes/edit/{id}")
    public ResponseEntity< RestauranteResponse<RestauranteDTO>> actualizarRestaurante(@PathVariable  Integer id, 
                                             @RequestBody Restaurante restaurante){
        try {
            Optional<Restaurante> optionalRestaurante = restauranteService.findRestauranteById(id);
            // Obtener el usuario autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
               RestauranteResponse<RestauranteDTO> response = new RestauranteResponse<>(
                    "error", "El usuario no está autorizado", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
    
            if (!optionalRestaurante.isPresent()) {
               RestauranteResponse<RestauranteDTO> response = new RestauranteResponse<>(
                 "error", "No se encontró un menú con el ID proporcionado", null);
                 return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            Restaurante existingRestaurante = optionalRestaurante.get();
            existingRestaurante.setNombre(restaurante.getNombre());
            existingRestaurante.setCiudad(restaurante.getCiudad());
            existingRestaurante.setProvincia(restaurante.getProvincia());
            existingRestaurante.setTelefono(restaurante.getTelefono());
            existingRestaurante.setDireccion(restaurante.getDireccion());
            // Guardar la nueva imagen si existe
            if (restaurante.getImagen() != null && !restaurante.getImagen().isEmpty()) {
                String newFileName = 
                       firebaseStorageService.updateFile(existingRestaurante.getImagen(), restaurante.getImagen());
                       existingRestaurante.setImagen(newFileName);
            }

            // Guarda el nuevo restaurante en la base de datos
            Optional<RestauranteDTO> nuevoRestaurante = restauranteService.save(existingRestaurante);
            RestauranteResponse<RestauranteDTO> response = new RestauranteResponse<>(
                "ok", "Restaurante actualizado con éxito", nuevoRestaurante.get());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            RestauranteResponse<RestauranteDTO> response = new RestauranteResponse<>(
                 "error", "Ocurrió un error al actualizar el restaurante: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @PatchMapping("/restaurantes/edit/{id}")
    public ResponseEntity<RestauranteResponse<RestauranteDTO>> actualizarRestaurantePatch(@PathVariable Integer id,  
                                                @RequestBody Map<String, Object> updates) {
        try {
            Optional<Restaurante> optionalRestaurante = restauranteService.findRestauranteById(id);
            // Obtener el usuario autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
               RestauranteResponse<RestauranteDTO> response = new RestauranteResponse<>(
                    "error", "El usuario no está autorizado", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
    
            if (!optionalRestaurante.isPresent()) {
               RestauranteResponse<RestauranteDTO> response = new RestauranteResponse<>(
                 "error", "No se encontró un restaurante con el ID proporcionado", null);
                 return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            Restaurante existingRestaurante = optionalRestaurante.get();
            // Actualizar cada campo proporcionado en el objeto JSON de actualizaciones
            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                String campo = entry.getKey();
                Object valor = entry.getValue();
                switch (campo) {
                    case "nombre":
                        if (valor.toString() != null || ! valor.toString().isEmpty()) {
                            existingRestaurante.setNombre(valor.toString());
                        }
                        break;
                    case "ciudad":
                        if (valor.toString() != null || ! valor.toString().isEmpty()) {
                            existingRestaurante.setCiudad(valor.toString());
                        }
                        break;
                    case "provincia":
                        if (valor.toString() != null || ! valor.toString().isEmpty()) {
                            existingRestaurante.setProvincia(valor.toString());
                        }
                        break;
                    case "telefono":
                        if (valor.toString() != null || ! valor.toString().isEmpty()) {
                            existingRestaurante.setTelefono(valor.toString());
                        }
                        break;
                    case "imagen":
                        // Guardar la nueva imagen si existe
                        if (valor.toString() != null && ! valor.toString().isEmpty()) {
                            String newFileName = firebaseStorageService.updateFile(
                                existingRestaurante.getImagen(), valor.toString());
                            existingRestaurante.setImagen(newFileName);
                        }
                        break;
                    case "direccion":
                        if (valor.toString() != null || ! valor.toString().isEmpty()) {
                            existingRestaurante.setDireccion(valor.toString());
                        }
                        break;
                    default:
                         //campos desconocidos
                         RestauranteResponse<RestauranteDTO> errorResponse = new RestauranteResponse<>(
                            "error", "No se encontró parametro: " + campo, null);
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
                }
            }
            // Guarda el nuevo restaurante en la base de datos
            Optional<RestauranteDTO> nuevoRestaurante = restauranteService.save(existingRestaurante);
            RestauranteResponse<RestauranteDTO> response = new RestauranteResponse<>(
                "ok", "Restaurante actualizado con éxito", nuevoRestaurante.get());
            return ResponseEntity.ok(response);

        }catch (Exception e) {
            RestauranteResponse<RestauranteDTO> response = new RestauranteResponse<>(
                 "error", "Ocurrió un error al actualizar el restaurante: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/restaurantes/delete/{id}")
    public ResponseEntity<RestauranteResponse<RestauranteDTO>> deleteMenu(@PathVariable Integer id) {
        try {
            // Buscar el restaurante por su ID
            Optional<Restaurante> restauranteOptional = restauranteService.findRestauranteById(id);

             if (restauranteOptional.isPresent()) {
                 Restaurante restaurate = restauranteOptional.get();
                 // Si se encuentra el restaurante, lo eliminamos de la base de datos
                 restauranteService.deleteById(restaurate.getId().intValue());
                 // Devolvemos una respuesta indicando que el restaurante fue eliminado correctamente
                RestauranteResponse<RestauranteDTO> response = new RestauranteResponse<>(
                     "ok", "El restaurante se eliminó correctamente", null);
                 return ResponseEntity.ok(response);
             } else {
                 // Si no se encuentra el restaurante con el ID proporcionado, devolver una respuesta indicando que no se encontró el restaurante
                RestauranteResponse<RestauranteDTO> errorResponse = new RestauranteResponse<>(
                     "error", "No se encontró un restaurante con el ID proporcionado", null);
                 return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
             }
         } catch (Exception e) {
            // Manejo de la excepción
            RestauranteResponse<RestauranteDTO> response = new RestauranteResponse<>(
                "error", "Ocurrió un error al eliminar el restaurante: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
         }   
    }

    @GetMapping("/restaurantes/usuario")
    public ResponseEntity<RestauranteResponse<List<RestauranteDTO>>> getAllRestaurantesCreatedByCurrentUser() {
        try {
            // Obtener el usuario autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
               RestauranteResponse<List<RestauranteDTO>> response = new RestauranteResponse<>(
                    "error", "El usuario no está autorizado", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            // Obtener el ID del usuario autenticado
            String currentUserId = String.valueOf(((Usuario) authentication.getPrincipal()).getId());
            List<RestauranteDTO> restaurantesCreatedByCurrentUser = restauranteService.getRestaurantesCreatedByUser(currentUserId);
            // Devolver una respuesta indicando que se listaron los restaurantes correctamente
            if (restaurantesCreatedByCurrentUser == null || restaurantesCreatedByCurrentUser.isEmpty()) {
               RestauranteResponse<List<RestauranteDTO>> response = new RestauranteResponse<>(
                "error", "El usuario no tiene Restaurantes en la base de datos", restaurantesCreatedByCurrentUser);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

             RestauranteResponse<List<RestauranteDTO>> response = new  RestauranteResponse<>(
                "ok", "Menús listados con éxito", restaurantesCreatedByCurrentUser);
                return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Manejo de la excepción
            RestauranteResponse<List<RestauranteDTO>> response = new RestauranteResponse<>(
                "error", "Ocurrió un error al listar los restaurantes: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
