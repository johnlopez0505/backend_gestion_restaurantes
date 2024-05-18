package com.john.backend_gestion_restaurantes.controladores;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.john.backend_gestion_restaurantes.modelos.Menu;
import com.john.backend_gestion_restaurantes.modelos.Restaurante;
import com.john.backend_gestion_restaurantes.servicios.menu.MenuService;
import com.john.backend_gestion_restaurantes.servicios.restaurante.RestauranteService;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api")
public class MenuController {

    @Autowired
    private MenuService menuService;
    
    @Autowired
    private RestauranteService restauranteService;

    // public MenuController(MenuService menuService, RestauranteService restauranteService){
    //     this.menuService = menuService;
    //     this.restauranteService = restauranteService;
    // }

    @GetMapping("/menus")
    public ResponseEntity<Object> findAll(){
        //System.out.println("ingresa en buscar todos los menus ");
        try {
            List<Menu> menus = menuService.findAll();
            Map<String, Object> response = new HashMap<>();
            if (!menus.isEmpty()) {
                response.put("result", "ok");
                response.put("menus", menus.stream().map(
                    menu -> Map.of("id", menu.getId(),
                                   "restauranteId", menu.getRestaurante().getId(),
                                   "usuarioId", menu.getRestaurante().getUsuario().getId(),
                                   "nombre", menu.getNombre(),
                                   "descripcion", menu.getDescripcion(),
                                   "precio", menu.getPrecio(),
                                   "imagen", menu.getImagen()
                                   )
                                )
                            );
                return ResponseEntity.ok(response);
            } else {
                response.put("result", "ok");
                response.put("details", "No se encontro ningun menú en la base de datos");
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
            errorResponse.put("get", "/api/menus");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/menus/{id}")
    public ResponseEntity<Object> findMenuById(@PathVariable Integer id){
        try {
            Optional<Menu> optionalMenu = menuService.findById(id);
            Map<String, Object> response = new HashMap<>();
            if (!optionalMenu.isPresent()) {
                // Manejo del caso en el que no se encuentra el menú
                response.put("result", "error");
                response.put("message", "No se encontró un menú con el ID proporcionado");
                return ResponseEntity.ok(response);
            }
            Menu existingMenu = optionalMenu.get();
            // Devuelve una respuesta exitosa con el menú recién creado
            response.put("result", "ok");
            response.put("menus", Map.of("id", existingMenu.getId(),
                                "restauranteId", existingMenu.getRestaurante().getId(),
                                "usuarioId", existingMenu.getRestaurante().getUsuario().getId(),
                                "nombre", existingMenu.getNombre(),
                                "descripcion", existingMenu.getDescripcion(),
                                "precio", existingMenu.getPrecio(),
                                "imagen", existingMenu.getImagen()
                                )
                            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Manejo de la excepción
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            errorResponse.put("message", "Se produjo un error al buscar el menú: " + e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("get", "/api/menus/{id}");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    } 

    @PostMapping("/menus/add")
    public ResponseEntity<Object> createMenu(@RequestHeader Integer id, @RequestBody Menu menu) {
        try {
            Optional<Restaurante> optionalRestaurante = restauranteService.findById(id);
            Map<String, Object> response = new HashMap<>();
            if (!optionalRestaurante.isPresent()) {
                // Manejo del caso en el que no se encuentra el restaurante
                response.put("result", "error");
                response.put("message", "No se encontró un menú con el ID proporcionado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            Restaurante restaurante = optionalRestaurante.get();
            menu.setRestaurante(restaurante);
            // Guarda el nuevo menú en tu base de datos
            Menu nuevoMenu = this.menuService.save(menu);
            // Devuelve una respuesta exitosa con el menú recién creado
            response.put("result", "ok");
            response.put("menus", Map.of("id", nuevoMenu.getId(),
                                "restauranteId", nuevoMenu.getRestaurante().getId(),
                                "usuarioId", nuevoMenu.getRestaurante().getUsuario().getId(),
                                "nombre", nuevoMenu.getNombre(),
                                "descripcion", nuevoMenu.getDescripcion(),
                                "precio", nuevoMenu.getPrecio(),
                                "imagen", nuevoMenu.getImagen()
                                )
                            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            // Manejo de la excepción
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            errorResponse.put("message", "Se produjo un error al agregar el menú: " + e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("post", "/api/menus");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/menus/edit")
    public ResponseEntity<Object> updateMenu(@RequestHeader Integer menuId, @RequestBody Menu menu) {
        try {
            Optional<Menu> optionalMenu = menuService.findById(menuId);
            Map<String, Object> response = new HashMap<>();
            if (!optionalMenu.isPresent()) {
                // Manejo del caso en el que no se encuentra el menu
                response.put("result", "error");
                response.put("message", "No se encontró un menú con el ID proporcionado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            Menu existingMenu = optionalMenu.get();
            existingMenu.setNombre(menu.getNombre());
            existingMenu.setDescripcion(menu.getDescripcion());
            existingMenu.setPrecio(menu.getPrecio());
            existingMenu.setImagen(menu.getImagen());
            // Guarda el nuevo menú en tu base de datos
            Menu nuevoMenu = menuService.save(existingMenu);
            // Devuelve una respuesta exitosa con el menú recién creado
            response.put("result", "ok");
            response.put("menus", Map.of("id", nuevoMenu.getId(),
                            "restauranteId", nuevoMenu.getRestaurante().getId(),
                            "usuarioId", nuevoMenu.getRestaurante().getUsuario().getId(),
                            "nombre", nuevoMenu.getNombre(),
                            "descripcion", nuevoMenu.getDescripcion(),
                            "precio", nuevoMenu.getPrecio(),
                            "imagen", nuevoMenu.getImagen()
                            )
                        );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            // Manejo de la excepción
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            errorResponse.put("message", "Se produjo un error al actualizar el menú: " + e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("put", "/api/menus");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PatchMapping("/menus/edit")
    public ResponseEntity<Object> patchMenu(@RequestHeader Integer id, @RequestBody Map<String, Object> updates) {
        try {
            Optional<Menu> optionalMenu = menuService.findById(id);
            //System.out.println("este es el menu consultado por id " + optionalMenu.get().toString());
            Map<String, Object> response = new HashMap<>();
            if (!optionalMenu.isPresent()) {
                // Manejo del caso en el que no se encuentra el restaurante
                response.put("result", "error");
                response.put("message", "No se encontró un menú con el ID proporcionado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            Menu menuExistente = optionalMenu.get();
            // Actualizar cada campo proporcionado en el objeto JSON de actualizaciones
            updates.forEach((campo, valor) -> {
                switch (campo) {
                    case "nombre":
                        menuExistente.setNombre(valor.toString());
                        break;
                    case "descripcion":
                        menuExistente.setDescripcion(valor.toString());
                        break;
                    case "precio":
                        menuExistente.setPrecio((Double)valor);
                        break;
                    case "imagen":
                        menuExistente.setImagen(valor.toString());
                        break;
                    default:
                        // Ignora campos desconocidos
                        //response.put(campo, "No se encontró parametro");
                        break;
                }
            });
            Menu editMenu = menuService.save(menuExistente);
            // Devuelve una respuesta exitosa con el menú recién creado
            response.put("result", "ok");
            response.put("menus", Map.of("id", editMenu.getId(),
                                "restauranteId", editMenu.getRestaurante().getId(),
                                "usuarioId", editMenu.getRestaurante().getUsuario().getId(),
                                "nombre", editMenu.getNombre(),
                                "descripcion", editMenu.getDescripcion(),
                                "precio", editMenu.getPrecio(),
                                "imagen", editMenu.getImagen()
                                )
                            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            // Manejo de la excepción
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            errorResponse.put("message", "Se produjo un error al actualizar el menú: " + e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("path", "/api/menus");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("/menus/delete/{id}")
    public ResponseEntity<Object> deleteMenu(@PathVariable Integer id) {
        try {
            // Buscar el menú por su ID
            Optional<Menu> menuOptional = menuService.findById(id);
            Map<String, Object> response = new HashMap<>();
            if (menuOptional.isPresent()) {
                Menu menu = menuOptional.get();
                // Si se encuentra el menú, eliminarlo de la base de datos
                menuService.deleteById(menu.getId().intValue());
                // Devolver una respuesta indicando que el menú fue eliminado correctamente
                response.put("result", "ok");
                response.put("message", "El menú se eliminó correctamente");
                return ResponseEntity.ok(response);
            } else {
                // Si no se encuentra el menú con el ID proporcionado, devolver una respuesta indicando que no se encontró el menú
                response.put("result", "error");
                response.put("message", "No se encontró un menú con el ID proporcionado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
             // Manejo de la excepción
             Map<String, Object> errorResponse = new HashMap<>();
             errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
             errorResponse.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
             errorResponse.put("message", "Se produjo un error al agregar el menú: " + e.getMessage());
             errorResponse.put("timestamp", LocalDateTime.now());
             errorResponse.put("delete", "/api/menus/{id}");
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }   
    }

    @GetMapping("/menus/usuario")
    public ResponseEntity<List<Menu>> getAllMenusCreatedByCurrentUser() {
        // Obtener el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        // Obtener los menús creados por el usuario actual
        List<Menu> menusCreatedByCurrentUser = menuService.getMenusCreatedByUser(currentUsername);

        return ResponseEntity.ok(menusCreatedByCurrentUser);
    }
}
