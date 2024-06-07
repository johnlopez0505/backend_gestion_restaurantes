package com.john.backend_gestion_restaurantes.controladores;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.john.backend_gestion_restaurantes.dto.MenuDTO;
import com.john.backend_gestion_restaurantes.dto.response.MenuResponse;
import com.john.backend_gestion_restaurantes.modelos.Menu;
import com.john.backend_gestion_restaurantes.modelos.Restaurante;
import com.john.backend_gestion_restaurantes.modelos.Usuario;
import com.john.backend_gestion_restaurantes.servicios.imagenes.FirebaseStorageService;
import com.john.backend_gestion_restaurantes.servicios.menu.MenuService;
import com.john.backend_gestion_restaurantes.servicios.restaurante.RestauranteService;

import java.io.IOException;
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

   @Autowired
   private FirebaseStorageService firebaseStorageService;

    @GetMapping("/menus")
    public ResponseEntity<MenuResponse<List<MenuDTO>>> findAll(){
        try {
            List<MenuDTO> menus = menuService.findAll();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                MenuResponse<List<MenuDTO>> response = new MenuResponse<>(
                    "error", "No autorizado", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            if (menus == null || menus.isEmpty()) {
            MenuResponse<List<MenuDTO>> response = new MenuResponse<>(
                "error", "No hay menús almacenados en la base de datos", menus);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            MenuResponse<List<MenuDTO>> response = new MenuResponse<>(
            "ok", "Menús listados con éxito", menus);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Manejo de la excepción
            MenuResponse<List<MenuDTO>> errorResponse = new MenuResponse<>(
                "error", "Se produjo un error al listar los menús: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
        

    @GetMapping("/menus/{id}")
    public ResponseEntity<MenuResponse<MenuDTO>> findMenuById(@PathVariable Integer id){
        try {
            Optional<MenuDTO> menu = menuService.findById(id);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                MenuResponse<MenuDTO> response = new MenuResponse<>(
                    "error", "El usuario no esta autorizado", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            if (!menu.isPresent()) {
            MenuResponse<MenuDTO> response = new MenuResponse<>(
                "error", "No se encontró un menú con el ID proporcionado", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            MenuResponse<MenuDTO>response = new MenuResponse<>(
            "ok", "Menús listado con éxito", menu.get());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Manejo de la excepción
            MenuResponse<MenuDTO> errorResponse = new MenuResponse<>(
                "error", "Se produjo un error al liostar el menú: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    } 

    @PostMapping("/menus/add")
    public ResponseEntity<MenuResponse<MenuDTO>> createMenu(@RequestHeader Integer id, @RequestBody Menu menu) {
        try {
            // Obtener el usuario autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                MenuResponse<MenuDTO> response = new MenuResponse<>(
                    "error", "El usuario no esta autorizado", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
    
            String userId = String.valueOf(((Usuario) authentication.getPrincipal()).getId());
            Optional<Restaurante> restauranteOptional = restauranteService.findRestauranteById(Integer.parseInt(userId));
            System.out.println(restauranteOptional);

            if (!restauranteOptional.isPresent()) {
            MenuResponse<MenuDTO> response = new MenuResponse<>(
                "error", "No se encontró un restaurante con el ID proporcionado", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Restaurante restaurante = restauranteOptional.get();
            menu.setRestaurante(restaurante);
            // Validar si el valor es un Double antes de realizar la conversión
            if (!(menu.getPrecio() instanceof Double)) {
                // Manejar el caso en el que el valor no sea un Double
                MenuResponse<MenuDTO> errorResponse = new MenuResponse<>(
                    "error", "El precio proporcionado no es un número válido debe tener decimales", null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            // Guardar la nueva imagen si existe
            if (menu.getImagen() != null && !menu.getImagen().isEmpty()) {
                String newFileName;
                try {
                    newFileName = firebaseStorageService.uploadBase64Image(menu.getImagen());
                } catch (Exception e) {
                    // Manejo de la excepción
                    throw new RuntimeException("Error al subir la imagen a Firebase", e);
                }
                menu.setImagen(newFileName);
            } else {
                menu.setImagen("ee563e42-e7c3-4734-8077-b99ad71dc145.jpeg");
            }
            Optional<MenuDTO> nuevoMenu = menuService.save(menu);
            MenuResponse<MenuDTO> response = new MenuResponse<>(
                "ok", "Menú creado con éxito", nuevoMenu.get());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Manejo de la excepción
            MenuResponse<MenuDTO> errorResponse = new MenuResponse<>(
                "error", "Se produjo un error al añadir el menú: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }




    @PutMapping("/menus/edit/{id}")
    public ResponseEntity<MenuResponse<MenuDTO>> updateMenu(@PathVariable  Integer id, @RequestBody Menu menu) {
        try {
            // Obtener el usuario autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                MenuResponse<MenuDTO> response = new MenuResponse<>(
                    "error", "El usuario no está autorizado", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
    
            Optional<Menu> menuOptional = menuService.findMenuById(id);
    
            if (!menuOptional.isPresent()) {
                MenuResponse<MenuDTO> response = new MenuResponse<>(
                 "error", "No se encontró un menú con el ID proporcionado", null);
                 return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            Menu existingMenu = menuOptional.get();
            existingMenu.setNombre(menu.getNombre());
            existingMenu.setDescripcion(menu.getDescripcion());
            existingMenu.setPrecio(menu.getPrecio());
            // Guardar la nueva imagen si existe
            if (menu.getImagen() != null && !menu.getImagen().isEmpty()) {
                String newFileName = 
                       firebaseStorageService.updateFile(existingMenu.getImagen(), menu.getImagen());
                       System.out.println(newFileName);
                existingMenu.setImagen(newFileName);
            }else{
                existingMenu.setImagen(existingMenu.getImagen());
            }
            // Validar si el valor es un Double antes de realizar la conversión
            if (!(menu.getPrecio() instanceof Double)) {
                // Manejar el caso en el que el valor no sea un Double
                MenuResponse<MenuDTO> errorResponse = new MenuResponse<>(
                    "error", "El precio proporcionado no es un número válido debe tener decimales", null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            // Guarda el nuevo menú en tu base de datos
            Optional<MenuDTO> nuevoMenu = menuService.save(existingMenu);
            MenuResponse<MenuDTO> response = new MenuResponse<>(
                "ok", "Menú actualizado con éxito", nuevoMenu.get());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Manejo de la excepción
            MenuResponse<MenuDTO> errorResponse = new MenuResponse<>(
                "error", "Se produjo un error al actualizar el menú: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

    }

    @PatchMapping("/menus/edit/{id}")
    public ResponseEntity<MenuResponse<MenuDTO>> patchMenu(@PathVariable Integer id, @RequestBody Map<String, Object> updates) {
        try {
              // Obtener el usuario autenticado
              Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
              if (authentication == null || !authentication.isAuthenticated()) {
                  MenuResponse<MenuDTO> response = new MenuResponse<>(
                      "error", "El usuario no está autorizado", null);
                  return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
              }
      
              Optional<Menu> menuOptional = menuService.findMenuById(id);
              System.out.println(menuOptional);
      
              if (!menuOptional.isPresent()) {
                  MenuResponse<MenuDTO> response = new MenuResponse<>(
                   "error", "No se encontró un menú con el ID proporcionado", null);
                   return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
              }
            
                Menu menuExistente = menuOptional.get();
                // Actualizar cada campo proporcionado en el objeto JSON de actualizaciones
                for (Map.Entry<String, Object> entry : updates.entrySet()) {
                    String campo = entry.getKey();
                    Object valor = entry.getValue();
                
                    switch (campo) {
                        case "nombre":
                            if (valor.toString() != null || ! valor.toString().isEmpty()) {
                                menuExistente.setNombre(valor.toString());
                            }
                            break;
                        case "descripcion":
                            if (valor.toString() != null || ! valor.toString().isEmpty()) {
                                menuExistente.setDescripcion(valor.toString());
                            }
                            break;
                        case "precio":
                            try {
                                if(valor.toString() != null || ! valor.toString().isEmpty()){
                                    menuExistente.setPrecio(Double.parseDouble(valor.toString()));
                                } 
                            } catch (NumberFormatException e) {
                                // Manejar el caso en el que el valor no sea un número válido
                                MenuResponse<MenuDTO> errorResponse = new MenuResponse<>(
                                    "error", "El precio proporcionado no es un número válido", null);
                                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
                            }
                            break;
                        case "imagen":
                            // Guardar la nueva imagen si existe
                            if (valor.toString() != null && ! valor.toString().isEmpty()) {
                                String newFileName = "";
                                try {
                                    newFileName = firebaseStorageService.updateFile(
                                        menuExistente.getImagen(), valor.toString());
                                } catch (IOException e) {
                                    // Manejamos el error al guardar la imagen
                                    MenuResponse<MenuDTO> errorResponse = new MenuResponse<>(
                                        "error", "Error al editar la imagen", null);
                                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
                                }
                                menuExistente.setImagen(newFileName);
                            }
                            break;
                        default:
                            MenuResponse<MenuDTO> errorResponse = new MenuResponse<>(
                                "error", "No se encontró parámetro: " + campo, null);
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
                    }
                };
                // Guarda el nuevo menú en tu base de datos
                Optional<MenuDTO> nuevoMenu = menuService.save(menuExistente);
                MenuResponse<MenuDTO> response = new MenuResponse<>(
                "ok", "Menú actualizado con éxito", nuevoMenu.get());
                return ResponseEntity.ok(response);
            } catch (Exception e) {
               // Manejo de la excepción
               MenuResponse<MenuDTO> errorResponse = new MenuResponse<>(
               "error", "Se produjo un error al actualizar el menú: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("/menus/delete/{id}")
    public ResponseEntity<MenuResponse<MenuDTO>> deleteMenu(@PathVariable Integer id) {
        try {
            // Buscamos el menú por su ID
            Optional<Menu> menuOptional = menuService.findMenuById(id);
            if (menuOptional.isPresent()) {
                Menu menu = menuOptional.get();
                // Si se encuentra el menú, lo eliminamos de la base de datos
                menuService.deleteById(menu.getId().intValue());
                // Devolvemos una respuesta indicando que el menú fue eliminado correctamente
                MenuResponse<MenuDTO> response = new MenuResponse<>(
                    "ok", "El menú se eliminó correctamente", null);
                return ResponseEntity.ok(response);
            } else {
                MenuResponse<MenuDTO> errorResponse = new MenuResponse<>(
                    "error", "No se encontró un menú con el ID proporcionado", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
        } catch (Exception e) {
             // Manejo de la excepción
             MenuResponse<MenuDTO> errorResponse = new MenuResponse<>(
                 "error", "Se produjo un error al eliminar el menú: " + e.getMessage(), null);
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }   
    }
    

    @GetMapping("/menus/usuario")
    public ResponseEntity<MenuResponse<List<MenuDTO>>>getAllMenusCreatedByCurrentUser() {
        try {
             // Obtener el usuario autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                MenuResponse<List<MenuDTO>> response = new MenuResponse<>(
                    "error", "No autorizado", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            String currentUserId = String.valueOf(((Usuario) authentication.getPrincipal()).getId());
            List<MenuDTO> menusCreatedByCurrentUser = menuService.getMenusCreatedByUser(currentUserId);

            if (menusCreatedByCurrentUser == null || menusCreatedByCurrentUser.isEmpty()) {
                System.out.println("entro si esta vacio o es null");
            MenuResponse<List<MenuDTO>> response = new MenuResponse<>(
                "error", "El usuario no tiene Menús en la base de datos", menusCreatedByCurrentUser);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            MenuResponse<List<MenuDTO>> response = new MenuResponse<>(
            "ok", "Menús listados con éxito", menusCreatedByCurrentUser);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
             // Manejo de la excepción
             MenuResponse<List<MenuDTO>> errorResponse = new MenuResponse<>(
                 "error", "Ocurrió un error al obtener los Menús del usuario" + e.getMessage(), null);
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
